package com.tibbers.zhonghui.service;

import com.tibbers.zhonghui.model.Refund;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/24 20:46
 * @description:
 */
public interface IRefundService {
    Map<String,Object> refundApply(MultipartFile[] files,String refundSerial);

    String dealWithNotifyRefund(String xmldata);

    List<Refund> queryUnconfirmRefunds(Map<String,Object> params);

    void updateRefundSerial(Refund refund);
}
