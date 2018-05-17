package com.tibbers.zhonghui.dao;

import com.tibbers.zhonghui.model.OrderItems;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/23 22:30
 * @description:
 */
@Repository
public interface IOrderItemsDao {

    void insertItemsBatch(List<OrderItems> list);

    List<Map<String, Object>> queryItemsByPager(Map<String,Object> params);
}
