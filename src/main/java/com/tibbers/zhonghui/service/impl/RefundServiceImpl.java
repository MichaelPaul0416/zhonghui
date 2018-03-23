package com.tibbers.zhonghui.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.tibbers.zhonghui.config.APIException;
import com.tibbers.zhonghui.config.AppConstants;
import com.tibbers.zhonghui.config.ServiceConfigBean;
import com.tibbers.zhonghui.config.WxPayConfiguration;
import com.tibbers.zhonghui.dao.IOrdersDao;
import com.tibbers.zhonghui.dao.IRefundCertsDao;
import com.tibbers.zhonghui.dao.IRefundDao;
import com.tibbers.zhonghui.model.Orders;
import com.tibbers.zhonghui.model.Refund;
import com.tibbers.zhonghui.model.RefundCerts;
import com.tibbers.zhonghui.service.IRefundService;
import com.tibbers.zhonghui.utils.EncryptUtil;
import com.tibbers.zhonghui.utils.StringUtil;
import com.tibbers.zhonghui.utils.WxLoginUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author: Paul
 * @time:2018/1/24 20:46
 * @description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RefundServiceImpl implements IRefundService {

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private IRefundDao refundDao;

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private IOrdersDao ordersDao;

    @Autowired
    private WxPayConfiguration configuration;

    @Autowired
    private ServiceConfigBean serviceConfigBean;

    @Autowired
    private IRefundCertsDao refundCertsDao;

    @Autowired
    private WxPayConfiguration wxPayConfiguration;


    @Override
    public Map<String,Object> refundApply(HttpServletRequest servletRequest, String refundSerial) {
        Refund refund = JSONObject.parseObject(refundSerial,Refund.class);
        WxPayRefundRequest refundRequest = assemblyRefundRequest(refund);
        Map<String,Object> resultMap = new HashMap<>();
        logger.info(String.format("即将发送给微信的退款申请[%s]",refundRequest));
        try {
            WxPayRefundResult refundResult = wxPayService.refund(refundRequest);
            logger.info(String.format("微信返回的退款信息[%s]",refundResult));
            if(AppConstants.RETURN_CODE.equals(refundResult.getReturnCode())){
                if(AppConstants.RESULT_CODE.equals(refundResult.getResultCode())){
                    refund.setRefundserialid(StringUtil.serialId());
                    refund.setApplydatetime(StringUtil.currentDateTime());
                    //商家对于退款流水的状态[0：拒绝退款，1：同意退款，2：退款成功，3：退款失败，4：退款中]
                    refund.setAgreestate("4");
                    refund.setReverse1(refundResult.getRefundId());//微信退款单号
                    refund.setReverse2(refundResult.getTransactionId());//微信订单号

                    refundDao.insertSingelRefund(refund);
                    logger.info(String.format("插入退款流水成功[%s]",refund));

                    logger.info("开始落地退款凭证");
                    String prefix = serviceConfigBean.getRefundcertsPathPrefix();
                    File dir = new File(prefix);
                    if(!dir.exists()){
                        logger.info(String.format("目录[%s]不存在，创建文件夹",prefix));
                    }

                    List<RefundCerts> refundCertss = new ArrayList<>();
                    List<String> paths = WxLoginUtil.upload(servletRequest,prefix);
                    logger.info(String.format("退款凭证落地成功[%s]",paths));
                    for(String certPath : paths){
                        RefundCerts refundCerts = new RefundCerts();
                        refundCerts.setSerialid(StringUtil.serialId());
                        refundCerts.setRefundserialid(refund.getRefundserialid());
                        refundCerts.setCertphotoid(StringUtil.generateUUID());
                        refundCerts.setCertphotopath(certPath);
                        refundCerts.setReverse1("");
                        refundCerts.setReverse2("");

                        refundCertss.add(refundCerts);
                    }
//                    for(MultipartFile multipartFile : files){
//                        String storePath = prefix + "\\" + refund.getRefundserialid();
//                        FileUtils.copyInputStreamToFile(multipartFile.getInputStream(),new File(storePath,multipartFile.getOriginalFilename()));
//                        logger.info(String.format("文件[%s]落地成功",storePath));
//
//                        RefundCerts refundCerts = new RefundCerts();
//                        refundCerts.setSerialid(StringUtil.serialId());
//                        refundCerts.setRefundserialid(refund.getRefundserialid());
//                        refundCerts.setCertphotoid(StringUtil.generateUUID());
//                        refundCerts.setCertphotopath(storePath + "\\" + multipartFile.getOriginalFilename());
//                        refundCerts.setReverse1("");
//                        refundCerts.setReverse2("");
//
//                        refundCertss.add(refundCerts);
//                    }
                    refundCertsDao.insertCertOrBatch(refundCertss);
                    logger.info(String.format("退款凭证信息插入成功,[%s]",refundCertss));

                }else{
                    logger.error(String.format("微信退款申请失败，错误原因[%s]",refundResult.getErrCodeDes()));
                }
                resultMap.put("errorcode",refundResult.getErrCode());
                resultMap.put("errormsg",refundResult.getErrCodeDes());
                resultMap.put("appid",refundResult.getAppid());
                resultMap.put("mch_id",refundResult.getMchId());
                resultMap.put("nonce_str",refundResult.getNonceStr());
                resultMap.put("sign",refundResult.getSign());
                resultMap.put("transaction_id",refundResult.getTransactionId());
                resultMap.put("out_trade_no",refundResult.getOutTradeNo());
                resultMap.put("out_refund_no",refundResult.getOutRefundNo());
                resultMap.put("refund_id",refundResult.getRefundId());
                resultMap.put("refund_fee",refundResult.getRefundFee());

            }else{
                resultMap.put("returncode",refundResult.getReturnCode());
                resultMap.put("returnmsg",refundResult.getReturnMsg());
            }
        } catch (WxPayException e) {
            logger.error(e.getMessage(),e);
            String errorcode = e.getReturnCode(),errormsg = e.getReturnMsg();
            if(StringUtils.isNoneEmpty(e.getErrCode())){
                errorcode = e.getErrCode();
                errormsg = e.getErrCodeDes();
            }
            String info = String.format("微信返回错误代码[%s],错误描述[%s]",errorcode,errormsg);
            throw new APIException(info);
        } catch (IOException e) {
            throw new APIException(e.getCause().getMessage());
        }
        return resultMap;
    }

    @Override
    public String dealWithNotifyRefund(String xmldata) {
        String response;
        try {
            WxPayRefundNotifyResult notifyResult = wxPayService.parseRefundNotifyResult(xmldata);
            if(AppConstants.RETURN_CODE.equals(notifyResult.getReturnCode())){//SUCCESS
                notifyResult.getReqInfo();
            }else{

            }
        } catch (WxPayException e) {
            throw new APIException(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Refund> queryUnconfirmRefunds(Map<String, Object> params) {
        logger.info(String.format("开始查询未明确的退款流水，查询参数[%s]",params));
        List<Refund> resultList = refundDao.queryRefundsByPager(params);
        logger.info(String.format("未明确的退款流水如下[%s]",resultList));
        return resultList;
    }

    @Override
    public void updateRefundSerial(Refund refund) {
        refundDao.uploadRefundSerialInfo(refund);
    }

    private WxPayRefundRequest assemblyRefundRequest(Refund refund){
        logger.info(String.format("开始组装退款流水参数"));
        WxPayRefundRequest refundRequest = WxPayRefundRequest.newBuilder().build();
        refundRequest.setAppid(configuration.getAppId());
        refundRequest.setMchId(configuration.getMchId());
        refundRequest.setNonceStr(StringUtil.randomStr(32));
        refundRequest.setOutTradeNo(refund.getOrderid());
        refundRequest.setOutRefundNo(StringUtil.serialId());

        Map<String,Object> params = new HashMap<>();
        Orders orders = new Orders();
        orders.setOrderid(refund.getOrderid());
        params.put("orders",orders);
        params.put("account",null);
        List<Map<String,String>> queryOrders = ordersDao.queryOrdersByPager(params);

        if(queryOrders.size() == 0){
            throw new APIException(String.format("数据库中未查询到订单[%s]",refund.getOrderid()));
        }else if(queryOrders.size() > 1){
            throw new APIException(String.format("数据库中订单[%s]重复，实际数量为[%s]笔,请联系管理员",refund.getOrderid(),queryOrders.size()));
        }

        String amount = queryOrders.get(0).get("amount");
        if(!amount.equals(refund.getAmount())){
            throw new APIException(String.format("订单[%s]金额与退款流水金额[%s]不一致",amount,refund.getAmount()));
        }
        refundRequest.setTotalFee((int)(StringUtil.formatStr2Dobule(refund.getAmount()) * 100));
        refundRequest.setRefundFee((int)(StringUtil.formatStr2Dobule(refund.getAmount()) * 100));
        refundRequest.setRefundDesc(refund.getDetail());

//        refundRequest.setSignType(AppConstants.SIGN_TYPE_MD5);
        Map<String,String> urlparams = new TreeMap<>();
        urlparams.put("appid",refundRequest.getAppid());
        urlparams.put("mch_id",refundRequest.getMchId());
        urlparams.put("nonce_str",refundRequest.getNonceStr());
        urlparams.put("out_trade_no",refundRequest.getOutTradeNo());
        urlparams.put("out_refund_no",refundRequest.getOutRefundNo());
        urlparams.put("total_fee",String.valueOf(refundRequest.getTotalFee()));
        urlparams.put("refund_fee",String.valueOf(refundRequest.getRefundFee()));
        urlparams.put("refund_desc",refundRequest.getRefundDesc());
//        urlparams.put("sign_type",refundRequest.getSignType());
//        urlparams.put("key",wxPayConfiguration.getMchKey());

        String sign = EncryptUtil.contactParams(urlparams);
        sign += "key=" + configuration.getMchKey();
        refundRequest.setSign(EncryptUtil.encodeMD5String(sign));

        return refundRequest;
    }
}
