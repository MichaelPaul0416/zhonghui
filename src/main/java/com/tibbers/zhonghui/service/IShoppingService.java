package com.tibbers.zhonghui.service;

import com.tibbers.zhonghui.model.common.Pager;

import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/21 16:26
 * @description:
 */
public interface IShoppingService {
    String addItem2ShopCar(String shoppingCar);

    void removeFromShopCar(String shoppingCar);

    void removeBatchFromShopCar(String list);

    void updateGoodsInShopCar(String shoppingCar);

    List<Map<String,String>> queryListByPager(String accountid, String salestate, String deleteflag, Pager pager);
}
