package com.tibbers.zhonghui.service.impl;

import com.alibaba.fastjson.JSONObject;
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
    public String addRefundApply(HttpServletRequest servletRequest, String refundSerial) {
        Refund refund = JSONObject.parseObject(refundSerial,Refund.class);
        logger.info(String.format("接受用户[%s]的退款申请",refund.getAccountid()));
        refund.setRefundserialid(StringUtil.serialId());
        refund.setApplydatetime(StringUtil.currentDateTime());
        //商家对于退款流水的状态[0：拒绝退款，1：同意退款，2：退款成功，3：退款失败，4：申请退款中]
        refund.setAgreestate("4");

        refundDao.insertSingelRefund(refund);
        logger.info(String.format("退款流水[%s]插入成功，等待退款审核",refund));
        logger.info("开始落地退款凭证");
        String prefix = serviceConfigBean.getRefundcertsPathPrefix();
        File dir = new File(prefix);
        if(!dir.exists()){
            logger.info(String.format("目录[%s]不存在，创建文件夹",prefix));
        }

        List<RefundCerts> refundCertss = new ArrayList<>();
        List<String> paths ;
        try {
            paths = WxLoginUtil.upload(servletRequest,prefix);
            logger.info(String.format("退款凭证落地成功[%s]",paths));
            if(paths == null || paths.size() == 0){
                throw new APIException(String.format("退款流水[%s]的退款凭证落地后路径为空，请联系管理员检查",refund.getRefundserialid()));
            }

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
            refundCertsDao.insertCertOrBatch(refundCertss);
            logger.info(String.format("退款凭证信息插入成功,[%s]",refundCertss));

            return refund.getRefundserialid();
        } catch (IOException e) {
            throw new APIException(e.getMessage(),e);
        }

    }

    @Override
    public Map<String, String> salerAuditRefundSerial(String refundSerialid, String state, String rejectreason) {
        Refund refund = refundDao.queryRefundBySerialid(refundSerialid);
        if(refund.getReverse2() == null){
            refund.setReverse2(" ");
        }
        if(refund == null || StringUtil.isEmpty(refund.getOrderid())){
            throw new APIException("退款流水" + refundSerialid + "不存在");
        }
        Map<String,String> resultMap = new HashMap<>();
        if("0".equals(state)){
            logger.info(String.format("卖家拒绝退款流水[%s]的申请",refundSerialid));
            Refund update = new Refund();
            update.setRefundserialid(refundSerialid);
            update.setAgreestate(state);
            if(StringUtil.isEmpty(rejectreason)){
                throw new APIException(String.format("拒绝退款流水[%s]的原因rejectreason不能为空",refundSerialid));
            }
            update.setRejectreason(rejectreason);
            update.setDealdatetime(StringUtil.currentDateTime());
            update.setAgreedatetime(StringUtil.currentDateTime());
            update.setReverse1("");
            update.setReverse2("");
            resultMap.put("flag","ok");
            resultMap.put("msg",String.format("退款申请[%s]已拒绝退款，更新成功",refundSerialid));
        }else {
            logger.info(String.format("卖家同意退款[%s]",refundSerialid));
            WxPayRefundRequest refundRequest = assemblyRefundRequest(refund);

            logger.info(String.format("即将发送给微信的退款申请[%s]",refundRequest));
            Refund update = new Refund();
            try {
                WxPayRefundResult refundResult = wxPayService.refund(refundRequest);
                logger.info(String.format("微信返回的退款信息[%s]",refundResult));
                if(AppConstants.RETURN_CODE.equals(refundResult.getReturnCode())){
                    update.setAgreedatetime(StringUtil.currentDateTime());
                    update.setDealdatetime(StringUtil.currentDateTime());
                    if(AppConstants.RESULT_CODE.equals(refundResult.getResultCode())){
                        update.setAgreestate("1");
                        update.setReverse1(refundResult.getRefundId());//微信退款单号
                        update.setReverse2(refundResult.getTransactionId());//微信订单号
                    }else{
                        logger.error(String.format("微信退款申请失败，错误原因[%s]",refundResult.getErrCodeDes()));
                        update.setAgreestate("3");
                    }
                    update.setRefundserialid(refundSerialid);
                    refundDao.uploadRefundSerialInfo(update);
                    logger.info(String.format("退款流水[%s]状态更新成功",update));
                    resultMap.put("errorcode",refundResult.getErrCode());
                    resultMap.put("errormsg",refundResult.getErrCodeDes());
                    resultMap.put("transaction_id",refundResult.getTransactionId());
                    resultMap.put("out_trade_no",refundResult.getOutTradeNo());
                    resultMap.put("out_refund_no",refundResult.getOutRefundNo());
                    resultMap.put("refund_id",refundResult.getRefundId());
                    resultMap.put("refund_fee",refundResult.getRefundFee());
                    resultMap.put("flag","ok");
                    resultMap.put("msg","微信退款申请成功");
                }else{
                    resultMap.put("flag","failed");
                    resultMap.put("msg",String.format("微信退款申请通讯失败,请重新审核该笔退款[%s]",refundSerialid));
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
            }
        }

        return resultMap;
    }

    @Override
    public String queryRefundResult(String refundid) {
        Map<String,String> map = new TreeMap<>();
        map.put("appid",wxPayConfiguration.getAppId());
        map.put("mch_id",wxPayConfiguration.getMchId());
        map.put("nonce_str",StringUtil.randomStr(32));
        map.put("out_refund_no",refundid);
        map.put("sign_type","MD5");

        String contactParams = EncryptUtil.contactParams(map);
        contactParams += "key=" + wxPayConfiguration.getMchKey();
        logger.info(String.format("param[%s]",contactParams));
        String sign = EncryptUtil.encodeMD5String(contactParams);
        StringBuilder builder = new StringBuilder();

        builder.append("<xml>").append("<appid>").append(map.get("appid")).append("</appid>");
        builder.append("<mch_id>").append(map.get("mch_id")).append("</mch_id>");
        builder.append("<nonce_str>").append(map.get("nonce_str")).append("</nonce_str>");
        builder.append("<out_refund_no>").append(map.get("out_refund_no")).append("</out_refund_no>");
        builder.append("<sign>").append(sign).append("</sign>");
        builder.append("<sign_type>").append("MD5").append("</sign_type>").append("</xml>");

        logger.info("xml-->" + builder.toString());
        Map<String,String> param = new HashMap<>();
        param.put("xml",builder.toString());
        String response = WxLoginUtil.sendPost("https://api.mch.weixin.qq.com/pay/refundquery",param);
        return response;
    }


    @Override
    public List<Map<String, Object>> queryUnconfirmRefunds(Map<String, Object> params) {
        logger.info(String.format("开始查询未明确的退款流水，查询参数[%s]",params));
        List<Map<String,Object>> resultList = refundDao.queryRefundsByPager(params);
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
        refundRequest.setOutRefundNo(refund.getRefundserialid());

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
        if(amount.compareTo(refund.getAmount()) < 0){
            throw new APIException(String.format("订单[%s]金额小于退款流水金额[%s]",amount,refund.getAmount()));
        }
        refundRequest.setTotalFee((int)(StringUtil.formatStr2Dobule(amount) * 100));
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
