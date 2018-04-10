package com.tibbers.zhonghui.scan;

import com.github.binarywang.wxpay.bean.result.WxPayRefundQueryResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.tibbers.zhonghui.config.APIException;
import com.tibbers.zhonghui.config.AppConstants;
import com.tibbers.zhonghui.config.ServiceConfigBean;
import com.tibbers.zhonghui.dao.IProductBelongDao;
import com.tibbers.zhonghui.model.Product;
import com.tibbers.zhonghui.model.ProductBelong;
import com.tibbers.zhonghui.model.Refund;
import com.tibbers.zhonghui.service.IRefundService;
import com.tibbers.zhonghui.utils.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/2/4 23:29
 * @description:
 */
public class RefundScanBuilder implements IScanBuilder{

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private IRefundService refundService;

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private ServiceConfigBean serviceConfigBean;

    @Autowired
    private IProductBelongDao productBelongDao;
    @Override
    public void doScan() {
        if(!StringUtils.isEmpty(serviceConfigBean.getRefundScanSwitch()) && "1".equals(serviceConfigBean.getRefundScanSwitch())){
            logger.info(String.format("退款结果查询任务启动，机器时间[%s]", StringUtil.currentDateTime()));
            List<Map<String,Object>> unconfirmList = queryUnconfirmRefund();
            if(unconfirmList != null && unconfirmList.size() > 0){
                for(Map<String,Object> unconfirmRefund : unconfirmList){
                    try {
                        //如果还需要其他四个参数appid,mch_id,nonce_str,sign那等测试加上
                        WxPayRefundQueryResult refundQueryResult = wxPayService.refundQuery("","", (String) unconfirmRefund.get("refundserialid"),"");//根据商户的退款流水号查询，唯一
                        logger.info(String.format("查询到微信根据商户退款流水[%s]的退款信息[%s]",unconfirmRefund.get("refundserialid"),refundQueryResult));
                        Refund updateRefund = new Refund();
                        if(AppConstants.RETURN_CODE.equals(refundQueryResult.getReturnCode())){
                            if(AppConstants.RESULT_CODE.equals(refundQueryResult.getResultCode())){

                                List<WxPayRefundQueryResult.RefundRecord> refundRecordList = refundQueryResult.getRefundRecords();
                                if(refundRecordList.size() > 1){
                                    throw new APIException(String.format("商户退款流水号[%s]查询的结果集不唯一",unconfirmRefund.get("refundserialid")));
                                }

                                WxPayRefundQueryResult.RefundRecord refundRecord = refundRecordList.get(0);
                                updateRefund.setRefundserialid((String) unconfirmRefund.get("refundserialid"));
                                //2：退款成功，3：退款失败，4：退款中
                                if("SUCCESS".equals(refundRecord.getRefundStatus())){
                                    updateRefund.setAgreestate("2");
                                    updateRefund.setAgreedatetime(StringUtil.currentDateTime());
//                                    updateRefund.setReverse1((String) unconfirmRefund.get("reverse1"));
                                    updateRefund.setReverse2((String) unconfirmRefund.get("reverse2"));
                                }else if("CHANGE".equals(refundRecord.getRefundStatus())){
                                    updateRefund.setAgreestate("3");
                                }

                                logger.info(String.format("即将更新退款流水信息[%s]",updateRefund));
                                refundService.updateRefundSerial(updateRefund);

                                logger.info(String.format("更新产品[%s]的库存余量",unconfirmRefund.get("productid")));
                                ProductBelong productBelong = new ProductBelong();
                                productBelong.setProductid((String) unconfirmRefund.get("productid"));
                                productBelong.setSalestate("1");
                                ProductBelong query = productBelongDao.queryBelongByProductid(productBelong);
                                productBelong.setRemaindernum(Integer.parseInt(String.valueOf(unconfirmRefund.get("number"))) + query.getRemaindernum());
                                productBelongDao.updateProductBelongRemaindernum(productBelong);
                                logger.info(String.format("更新产品[%s]的库存余量[%]为[%s]",unconfirmRefund.get("productid"),query.getRemaindernum(),productBelong.getRemaindernum()));

                            }else{
                                //退款查询失败
                                logger.error(String.format("商户退款流水号[%s]对应的退款流水查询失败，微信返回报错信息[%s]，等待下次查询",unconfirmRefund.get("refundserialid"),refundQueryResult.getErrCodeDes()));
                            }
                        }else{
                            logger.error(String.format("商户退款流水号[%s]对应的流水本次通讯交互失败，微信返回报错信息[%s],等待下次查询",unconfirmRefund.get("refundserialid"),refundQueryResult.getReturnMsg()));
                        }
                    } catch (WxPayException e) {
                        logger.error(e.getMessage(),e);
                    }
                }
            }
        }

    }

    private List<Map<String,Object>> queryUnconfirmRefund(){
        Map<String,Object> params = new HashMap<>();
        Refund refund = new Refund();
        refund.setAgreestate("1");//卖家同意退款之后，状态由4[申请退款中]-->1[同意退款]
        params.put("refund",refund);
        List<Map<String,Object>> refundList = refundService.queryUnconfirmRefunds(params);
        return refundList;
    }

}

interface IScanBuilder{
    void doScan();
}
