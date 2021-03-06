package com.tibbers.zhonghui.service;

import com.tibbers.zhonghui.model.Orders;
import com.tibbers.zhonghui.model.common.Pager;
import com.tibbers.zhonghui.model.common.PayResult;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/24 19:44
 * @description:
 */
@Repository
public interface IOrderService {

    PayResult createOrder(String orderInfo, String itemlist, String itemtransportlist, String code);

    String dealWithAsynNotifyOrder(String xmlData);

    Map<String, List<Map<String, Object>>> accountOrderCenter(String accountid, String orderstate, Pager pager);

    Map<String, List<Map<String, Object>>> merchantQueryOrders(String accountid, String orderstate, Pager pager);

    Orders updateOrderInfo(Orders orders);

    String queryOrderState(String orderid);

    Map<String,String> cancelPayOrder(String accountid, String orderid);
}
