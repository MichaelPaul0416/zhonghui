package com.tibbers.zhonghui.controller;

import com.alibaba.fastjson.JSONObject;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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


    @RequestMapping("/refundApply")
    @ResponseBody
    public String refundApply(HttpServletRequest httpServletRequest){
        APIResponse apiResponse;
        Response response;

        String refundSerial = httpServletRequest.getParameter("refundSerial");
        if(!StringUtil.isEmpty(refundSerial)){
            try{
                Map<String,Object> resultMap = refundService.refundApply(httpServletRequest,refundSerial);
                response = new Response(true, resultMap);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE, AppConstants.SERVICE_SUCCEED_MESSAGE, response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getCause().getMessage());
                apiResponse =new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else {
            response = new Response(false,"退款流水信息不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
        }

        return JSONObject.toJSONString(apiResponse);

    }
//    public String refundApply(@RequestParam("refundcerts")MultipartFile[] file, String refundSerial){//退款productid的数量number需要前端传入，amount前端计算好
//        APIResponse apiResponse;
//        Response response;
//
//        if(StringUtil.argsNotEmpty(new String[]{refundSerial})){
//            try {
//                Map<String, Object> resultMap = refundService.refundApply(file, refundSerial);
//                response = new Response(true, resultMap);
//                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE, AppConstants.SERVICE_SUCCEED_MESSAGE, response);
//            }catch (APIException e){
//                logger.error(e.getMessage(),e);
//                response = new Response(false,e.getMessage());
//                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
//            }
//        }else{
//            response = new Response(false,"退款流水信息不能为空");
//            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
//        }
//
//        return String.valueOf(JSONObject.toJSON(apiResponse));
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
