package com.tibbers.zhonghui.dao;

import com.tibbers.zhonghui.model.Orders;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/23 22:28
 * @description:
 */
@Repository
public interface IOrdersDao {

    void insertSingelOrder(Orders orders);

    void updatePartOrderMsg(Orders orders);

    List<Orders> queryOrdersByPager(Map<String,Object> params);
}
