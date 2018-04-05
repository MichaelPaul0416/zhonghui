package com.tibbers.zhonghui.controller;

import com.alibaba.fastjson.JSONObject;

import com.tibbers.zhonghui.config.APIException;
import com.tibbers.zhonghui.config.AppConstants;
import com.tibbers.zhonghui.model.ReceiveAddress;
import com.tibbers.zhonghui.model.common.APIResponse;
import com.tibbers.zhonghui.model.common.Pager;
import com.tibbers.zhonghui.model.common.Response;
import com.tibbers.zhonghui.service.IReceiveAddressSerivce;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/22 23:27
 * @description:
 */
@Controller
@RequestMapping("receiveAddress")
public class ReceiveAddressController {

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private IReceiveAddressSerivce receiveAddressSerivce;

    @RequestMapping("/addOneReceiveAddress")
    @ResponseBody
    public String addOneReceiveAddress(String addressinfo, String isdefault){
        APIResponse apiResponse;
        Response response;
        if(!StringUtils.isEmpty(addressinfo)){
            try{
                //0:不是默认收货地址，1:默认收货地址
                String addressid = receiveAddressSerivce.addOneReceiveAddress(addressinfo,"1".equals(isdefault));
                Map<String,String> resultMap = new HashMap<>();
                resultMap.put("addressid",addressid);
                resultMap.put("msg","新增收货地址成功");
                response = new Response(true,resultMap);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE, AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (APIException e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else {
            response = new Response(false,"收货地址信息对象不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return String.valueOf(JSONObject.toJSON(apiResponse));
    }

    @RequestMapping("/updateReceiveAddress")
    @ResponseBody
    public String updateReceiveAddress(String addressinfo){
        APIResponse apiResponse;
        Response response;
        if(!StringUtils.isEmpty(addressinfo)){
            try{
                receiveAddressSerivce.updateReceiveAddress(addressinfo);
                response = new Response(true,"修改收货地址信息成功");
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (APIException e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else{
            response = new Response(false,"需要更新的收货地址信息对象不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return String.valueOf(JSONObject.toJSON(apiResponse));
    }

    @RequestMapping("/deleteReceiveAddress")
    @ResponseBody
    public String deleteReceiveAddress(String serialid,String accountid){
        APIResponse apiResponse ;
        Response response;
        if(!StringUtils.isEmpty(serialid) && !StringUtils.isEmpty(accountid)){
            try{
                receiveAddressSerivce.deleteReceiveAddress(serialid,accountid);
                response = new Response(true,String.format("账号[%s]的收货地址信息删除成功",accountid));
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (APIException e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else{
            response = new Response(false,"输入的地址id和账户id不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return String.valueOf(JSONObject.toJSON(apiResponse));
    }

    @RequestMapping("/queryAddressByPager")
    @ResponseBody
    public String queryAddressByPager(String accountid, String startLine, String offset){
        APIResponse apiResponse;
        Response response;
        if(!StringUtils.isEmpty(accountid)){
            Pager pager = null;
            try {
                if (!StringUtils.isEmpty(startLine) && !StringUtils.isEmpty(offset)) {
                    pager = new Pager(Integer.parseInt(startLine), Integer.parseInt(offset));
                }
                List<ReceiveAddress> list = receiveAddressSerivce.queryAddressByPager(accountid, pager);
                response = new Response(true,list);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else{
            response = new Response(false,"输入的账户id不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return String.valueOf(JSONObject.toJSON(apiResponse));
    }

}
