package com.tibbers.zhonghui.controller;

import com.alibaba.fastjson.JSONObject;
import com.sun.istack.internal.Nullable;
import com.tibbers.zhonghui.config.AppConstants;
import com.tibbers.zhonghui.model.common.APIResponse;
import com.tibbers.zhonghui.model.common.Response;
import com.tibbers.zhonghui.service.IRefundService;
import com.tibbers.zhonghui.utils.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Null;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/24 20:45
 * @description:用于退款或者银行异步通知
 */
@Controller
@RequestMapping("refund")
public class RefundController {

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private IRefundService refundService;

    /**
     * 1、交易时间超过一年的订单无法提交退款
     * 2、微信支付退款支持单笔交易分多次退款，多次退款需要提交原支付订单的商户订单号和设置不同的退款单号。申请退款总金额不能超过订单金额。 一笔退款失败后重新提交，请不要更换退款单号，请使用原商户退款单号
     */

    @RequestMapping("/addRefundApply")
    @ResponseBody
    public String addRefundApply(HttpServletRequest request,String refundSerial){
        APIResponse apiResponse;
        Response response;

        if(!StringUtil.isEmpty(refundSerial)){
            try{
                String serial = refundService.addRefundApply(request,refundSerial);
                Map<String,String> map = new HashMap<>();
                map.put("flag","ok");
                map.put("refundid",serial);
                response = new Response(true,map);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getCause().getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else {
            response = new Response( false,"退款流水refundSerial不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
        }

        return JSONObject.toJSONString(apiResponse);
    }

    @RequestMapping("/auditRefundApply")
    @ResponseBody
    public String auditRefundApply(String refundserialid, String refundstate, @Nullable String rejectreason){
        APIResponse apiResponse ;
        Response response;

        if(StringUtil.argsNotEmpty(new String[]{refundserialid,refundstate})){
            try{
                Map<String,String> result = refundService.salerAuditRefundSerial(refundserialid,refundstate,rejectreason);
                response = new Response(true,result);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getCause().getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else {
            response = new Response(false,"退款流水号refundserialid,退款确认标志refundstate不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
        }

        return JSONObject.toJSONString(apiResponse);

    }

//    @RequestMapping("/refundApply")
//    @ResponseBody
//    public String refundApply(HttpServletRequest httpServletRequest){
//        APIResponse apiResponse;
//        Response response;
//
//        String refundSerial = httpServletRequest.getParameter("refundSerial");
//        if(!StringUtil.isEmpty(refundSerial)){
//            try{
//                Map<String,Object> resultMap = refundService.refundApply(httpServletRequest,refundSerial);
//                response = new Response(true, resultMap);
//                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE, AppConstants.SERVICE_SUCCEED_MESSAGE, response);
//            }catch (Exception e){
//                logger.error(e.getMessage(),e);
//                response = new Response(false,e.getCause().getMessage());
//                apiResponse =new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
//            }
//        }else {
//            response = new Response(false,"退款流水信息不能为空");
//            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
//        }
//
//        return JSONObject.toJSONString(apiResponse);
//
//    }

    @RequestMapping("/refundNotifyResult")
    @ResponseBody
    public void refundNotifyResuult(HttpServletRequest request, HttpServletResponse response){
        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String line;
            StringBuilder builder = new StringBuilder();
            while((line = reader.readLine()) != null){
                builder.append(line);
            }
            String xmlData = builder.toString();
            logger.info(String.format("收到微信服务端的异步退款结果通知[%s]",xmlData));


        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        }
    }
}
