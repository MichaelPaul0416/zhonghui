package com.tibbers.zhonghui.controller;

import com.alibaba.fastjson.JSONObject;

import com.tibbers.zhonghui.config.APIException;
import com.tibbers.zhonghui.config.AppConstants;
import com.tibbers.zhonghui.model.common.APIResponse;
import com.tibbers.zhonghui.model.common.Pager;
import com.tibbers.zhonghui.model.common.Response;
import com.tibbers.zhonghui.service.IShoppingService;
import com.tibbers.zhonghui.utils.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/21 17:00
 * @description:
 */
@Controller
@RequestMapping("shoppingcar")
public class ShoppingCarController {

    private Logger logger = Logger.getLogger(getClass());


    @Autowired
    private IShoppingService shoppingService;


    @RequestMapping("/addItem2ShopCar")
    @ResponseBody
    public String addItem2ShopCar(String shopcar){
        APIResponse apiResponse;
        Response response;
        if(!StringUtils.isEmpty(shopcar)){
            try {
                String serialid = shoppingService.addItem2ShopCar(shopcar);
                response = new Response(true, serialid);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (APIException e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else {
            response = new Response(false,"新增购物车明细为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return  String.valueOf(JSONObject.toJSON(apiResponse));
    }

    @RequestMapping("/removeFromShopCar")
    @ResponseBody
    public String removeFromShopCar(String serialid){
        APIResponse apiResponse;
        Response response ;
        if(!StringUtils.isEmpty(serialid)){
            try {

                shoppingService.removeFromShopCar(serialid);
                response = new Response(true,"从购物车中移除明细成功");
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (APIException e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else{
            response = new Response(false,"输入的序列号为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return String.valueOf(JSONObject.toJSON(apiResponse));
    }

    @RequestMapping("/removeBatchFromShopCar")
    @ResponseBody
    public String removeBatchFromShopCar(String serialids){
        APIResponse apiResponse;
        Response response;
        if(!StringUtils.isEmpty(serialids)){
            try{
                shoppingService.removeBatchFromShopCar(serialids);
                response = new Response(true,"从购物车中批量移除商品成功");
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (APIException e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else{
            response = new Response(false,"需要删除的序列id为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return String.valueOf(JSONObject.toJSON(apiResponse));
    }

    @RequestMapping("/updateGoodsInShopCar")
    @ResponseBody
    public String updateGoodsInShopCar(String updateShopCar){
        APIResponse apiResponse ;
        Response response;
        if(!StringUtils.isEmpty(updateShopCar)){
            try {
                shoppingService.updateGoodsInShopCar(updateShopCar);
                response = new Response(true,"更新购物车信息成功");
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (APIException e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else{
            response = new Response(false,"输入的购物车明细信息为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return String.valueOf(JSONObject.toJSON(apiResponse));
    }

    @RequestMapping("/queryListByPager")
    @ResponseBody
    public String queryListByPager(String accountid, String salestate, String deleteflag,String startLine,String offset){
        APIResponse apiResponse ;
        Response response;
        if(!StringUtils.isEmpty(accountid) && !StringUtils.isEmpty(salestate)){
            Pager pager = null;
            if(!StringUtils.isEmpty(startLine) && !StringUtil.isEmpty(offset)){
                pager = new Pager(Integer.parseInt(startLine),Integer.parseInt(offset));
            }
            try{
                List<Map<String,String>> datas = shoppingService.queryListByPager(accountid,salestate, deleteflag, pager);
                response = new Response(true,datas);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (APIException e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else{
            response = new Response(false,"入参accountid与salestate不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return String.valueOf(JSONObject.toJSON(apiResponse));
    }
}
