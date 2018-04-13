package com.tibbers.zhonghui.dao;

import com.tibbers.zhonghui.model.OrderTransport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Author: wangqiang20995
 * @Date:2018/4/12
 * @Description:
 * @Resource:
 */
@Repository
public interface IOrderTransportDao {
    void insertSingleOrderTrasportRelation(OrderTransport orderTransport);

    void insertatchOrderTransportRelation(List<OrderTransport> list);

    void updateRelationState(String serialid,String state);

    List<OrderTransport> queryOrderTransportfees(Map<String,String> param);

    List<Map<String,String>> queryDetailTransportFeeInOrder(String orderid);

    void updateRelationStateByOrderid(OrderTransport orderTransport);
}
