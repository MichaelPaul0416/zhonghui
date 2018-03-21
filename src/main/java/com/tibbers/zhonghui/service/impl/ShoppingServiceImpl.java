package com.tibbers.zhonghui.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.tibbers.zhonghui.config.APIException;
import com.tibbers.zhonghui.dao.IShoppingCarDao;
import com.tibbers.zhonghui.model.ShoppingCar;
import com.tibbers.zhonghui.model.common.Pager;
import com.tibbers.zhonghui.service.IShoppingService;
import com.tibbers.zhonghui.utils.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/21 16:26
 * @description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ShoppingServiceImpl implements IShoppingService {
    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private IShoppingCarDao shoppingCarDao;

    @Override
    public String addItem2ShopCar(String shoppingCarInfo) {
        try {
            ShoppingCar shoppingCar = JSONObject.parseObject(shoppingCarInfo,ShoppingCar.class);
            logger.info(String.format("购物车中新增一条明细记录[%s]",shoppingCar));
            shoppingCar.setSerialid(StringUtil.generateUUID());
            shoppingCar.setDeleteflag("1");
            shoppingCarDao.addItem2ShopCar(shoppingCar);
            logger.info("明细新增成功");
            return shoppingCar.getSerialid();
        }catch (Exception e){
            throw new APIException(e.getCause());
        }
    }

    @Override
    public void removeFromShopCar(String serialid) {
        try{
            ShoppingCar shoppingCar = new ShoppingCar();
            shoppingCar.setSerialid(serialid);
            shoppingCar.setDeleteflag("0");
            logger.info(String.format("购物车中移除serialid=[%s]的明细记录",shoppingCar.getSerialid()));
            shoppingCarDao.removeFromShopCar(shoppingCar);
            logger.info("明细删除成功");
        }catch (Exception e){
            throw new APIException(e.getCause());
        }
    }

    @Override
    public void removeBatchFromShopCar(String list) {
        try{
            List<ShoppingCar> shoppingCarList = new ArrayList<>();
            String[] serialids = list.split(",");
            for(String serialid : serialids){
                ShoppingCar shoppingCar = new ShoppingCar();
                shoppingCar.setSerialid(serialid);
                shoppingCarList.add(shoppingCar);
            }
            logger.info(String.format("从购物车中删除[%s]条明细记录",shoppingCarList.size()));
            shoppingCarDao.removeBatchFromShopCar(shoppingCarList);
            logger.info(String.format("从购物车中共删除[%s]条明细记录",shoppingCarList.size()));
        }catch (Exception e){
            throw new APIException(e.getCause());
        }
    }

    @Override
    public void updateGoodsInShopCar(String shoppingCarInfo) {
        try{
            ShoppingCar shoppingCar = JSONObject.parseObject(shoppingCarInfo,ShoppingCar.class);
            logger.info(String.format("更新购物车记录信息[%s]",shoppingCar));
            if(shoppingCar.getNumber() < 0){
                logger.info(String.format("购物车明细[%s]对应的商品数量已经为0，从购物车中删除",shoppingCar.getSerialid()));
                shoppingCar.setDeleteflag("0");
                shoppingCarDao.removeFromShopCar(shoppingCar);
            }else {
                shoppingCar.setModifydatetime(StringUtil.currentDateTime());
                shoppingCarDao.updateGoodsInShopCar(shoppingCar);
                logger.info(String.format("购物车明细信息[%s]更新成功",shoppingCar.getSerialid()));
            }
        }catch (Exception e){
            throw new APIException(e.getCause());
        }
    }

    @Override
    public List<Map<String,String>> queryListByPager(String accountid, String salestate, String deleteflag, Pager pager) {
        Map<String,Object> map = new HashMap<>();
        map.put("accountid",accountid);
        map.put("salestate",salestate);
        map.put("pager",pager);
        map.put("deleteflag",deleteflag);

        try{
            return shoppingCarDao.queryListByPager(map);
        }catch (Exception e){
            throw new APIException(e.getCause());
        }

    }
}
