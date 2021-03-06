package com.tibbers.zhonghui.dao;

import com.tibbers.zhonghui.model.Refund;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/23 22:31
 * @description:
 */
@Repository
public interface IRefundDao {
    void insertSingelRefund(Refund refund);

    List<Map<String,Object>> queryRefundsByPager(Map<String,Object> params);

    void uploadRefundSerialInfo(Refund refund);//部分字段允许修改

    List<Map<String,Object>> refundSerialsInCenter(Map<String,Object> params);

    //商户查看待退款流水
    List<Map<String,Object>> merchantQueryRefundOrders(Map<String,Object> params);

    Refund queryRefundBySerialid(String refundserialid);
}
