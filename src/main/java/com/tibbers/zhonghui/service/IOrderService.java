package com.tibbers.zhonghui.service;

import com.tibbers.zhonghui.model.common.PayResult;
import org.springframework.stereotype.Repository;

/**
 * @author: Paul
 * @time:2018/1/24 19:44
 * @description:
 */
@Repository
public interface IOrderService {

    PayResult createOrder(String orderInfo, String itemlist, String code, String clientip, String recommandinfo);

    String dealWithAsynNotifyOrder(String xmlData);
}
