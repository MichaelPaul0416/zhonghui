package com.tibbers.zhonghui.service;

import com.tibbers.zhonghui.model.Refund;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/24 20:46
 * @description:
 */
public interface IRefundService {
//    Map<String,Object> refundApply(HttpServletRequest servletRequest, String refundSerial);

//    String dealWithNotifyRefund(String xmldata);

    List<Map<String, Object>> queryUnconfirmRefunds(Map<String,Object> params);

    void updateRefundSerial(Refund refund);

    String addRefundApply(HttpServletRequest servletRequest,String refundSerial);

    Map<String, String> salerAuditRefundSerial(String refundSerialid, String state, String rejectreason);

    String queryRefundResult(String refundid);

}
