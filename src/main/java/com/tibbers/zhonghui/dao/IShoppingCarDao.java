package com.tibbers.zhonghui.dao;

import com.tibbers.zhonghui.model.ShoppingCar;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/21 16:25
 * @description:
 */
@Repository
public interface IShoppingCarDao {

    void addItem2ShopCar(ShoppingCar shoppingCar);

    void removeFromShopCar(ShoppingCar shoppingCar);

    void removeBatchFromShopCar(List<ShoppingCar> list);

    void updateGoodsInShopCar(ShoppingCar shoppingCar);

    //查询购物车中明细，商品状态为[1:未删除,并且商品在售，2:未删除，但是商品售罄，3：未删除，但是商品已下架]
    List<Map<String,String>> queryListByPager(Map<String,Object> map);

    List<ShoppingCar> queryListBySerialid(List<String> list);

    ShoppingCar queryExists(ShoppingCar shoppingCar);
}
