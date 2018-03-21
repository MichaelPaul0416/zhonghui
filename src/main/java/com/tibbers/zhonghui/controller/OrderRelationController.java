package com.tibbers.zhonghui.controller;

import com.alibaba.fastjson.JSONObject;
import com.sun.istack.internal.Nullable;
import com.tibbers.zhonghui.config.APIException;
import com.tibbers.zhonghui.config.AppConstants;
import com.tibbers.zhonghui.model.common.APIResponse;
import com.tibbers.zhonghui.model.common.Pager;
import com.tibbers.zhonghui.model.common.PayResult;
import com.tibbers.zhonghui.model.common.Response;
import com.tibbers.zhonghui.service.IOrderService;
import com.tibbers.zhonghui.utils.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/24 20:11
 * @description:
 */
@Controller
@RequestMapping("orderrelation")
public class OrderRelationController {

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private IOrderService orderService;

    @RequestMapping("/salerOrderCenter")
    @ResponseBody
    public String salerOrderCenter(String accountid,@Nullable String orderstate,@Nullable String startLine,@Nullable String offset){
        APIResponse apiResponse;
        Response response;

        if(!StringUtil.isEmpty(accountid)){
            try{
                String state = null;
                if(!StringUtil.isEmpty(orderstate)){
                    state = orderstate;
                }
                Pager pager = null;
                if(!StringUtil.isEmpty(startLine) && !StringUtil.isEmpty(offset)){
                    pager = new Pager(Integer.parseInt(startLine),Integer.parseInt(offset));
                }

                List<Map<String,Object>> result = orderService.merchantQueryOrders(accountid,state,pager);
                response = new Response(true,result);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else {
            response = new Response(false,"卖家编号accountid不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
        }

        return JSONObject.toJSONString(apiResponse);
    }


    @RequestMapping("/accountOrderCenter")
    @ResponseBody
    public String accountOrderCenter(String accountid, @Nullable String orderstate,@Nullable String startLine,@Nullable String offset){
        APIResponse apiResponse;
        Response response;

        if(!StringUtil.isEmpty(accountid)){
            try{
                String orderState = null;
                if(!StringUtil.isEmpty(orderstate)){
                    orderState = orderstate;
                }

                Pager pager = null;
                if(!StringUtil.isEmpty(startLine) && !StringUtil.isEmpty(offset)){
                    pager = new Pager(Integer.parseInt(startLine),Integer.parseInt(offset));
                }

                List<Map<String,Object>> result = orderService.accountOrderCenter(accountid,orderState,pager);
                response = new Response(true,result);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else {
            response = new Response(false,"查询账户编号accountid不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return JSONObject.toJSONString(apiResponse);
    }

    @RequestMapping("/createSingleOrder")
    @ResponseBody
    public String createSingleOrder(String orderinfo,String itemlist,String code,String clientip,String recommandinfo){
        APIResponse apiResponse ;
        Response response;

        if(StringUtil.argsNotEmpty(new String[]{orderinfo,itemlist,code,clientip})){
            try{
                PayResult payResult = orderService.createOrder(orderinfo,itemlist,code,clientip,recommandinfo);
                response = new Response(true,payResult);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (APIException e){
                response = new Response(false,e.getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else{
            response = new Response(false,"订单信息[orderinfo],订单明细[itemlist],小程序code[code],客户端ip[clientip]不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return String.valueOf(JSONObject.toJSON(apiResponse));
    }

    @RequestMapping("/notifyOrderResult")
    @ResponseBody
    public void notifyOrderResult(HttpServletRequest request, HttpServletResponse response){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String line ;
            StringBuilder builder = new StringBuilder();
            while((line = reader.readLine()) != null){
                builder.append(line);
            }
            String notifyXml = builder.toString();
            logger.info(String.format("收到微信异步通知支付结果报文[%s]",notifyXml));
            String responseMsg = orderService.dealWithAsynNotifyOrder(notifyXml);
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            out.write(responseMsg.getBytes());
            out.flush();
            out.close();
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        }finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(),e);
                }
            }
        }
    }
}
