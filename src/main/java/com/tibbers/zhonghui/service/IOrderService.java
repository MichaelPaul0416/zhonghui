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

    PayResult createOrder(String orderInfo, String itemlist, String code, String clientip);

    String dealWithAsynNotifyOrder(String xmlData);

    List<Map<String,Object>> accountOrderCenter(String accountid, String orderstate, Pager pager);

    List<Map<String,Object>> merchantQueryOrders(String accountid,String orderstate,Pager pager);

    Orders updateOrderInfo(Orders orders);

    String queryOrderState(String orderid);
}
