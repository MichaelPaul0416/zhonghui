package com.tibbers.zhonghui.controller;

import com.alibaba.fastjson.JSONObject;
import com.sun.istack.internal.Nullable;
import com.tibbers.zhonghui.config.AppConstants;
import com.tibbers.zhonghui.model.WithDraw;
import com.tibbers.zhonghui.model.common.APIResponse;
import com.tibbers.zhonghui.model.common.Pager;
import com.tibbers.zhonghui.model.common.Response;
import com.tibbers.zhonghui.service.IWithDrawService;
import com.tibbers.zhonghui.utils.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wangqiang20995
 * @Date:2018/3/20
 * @Description:
 * @Resource:
 */
@Controller
@RequestMapping("withdraw")
public class WithDrawController {

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private IWithDrawService withDrawService;

    @RequestMapping("/applyWithDrawOrBatch")
    @ResponseBody
    public String applyWithDrawOrBatch(String withdraws){
        APIResponse apiResponse;
        Response response;

        if(!StringUtil.isEmpty(withdraws)){
            try{
                List<WithDraw> list = JSONObject.parseArray(withdraws,WithDraw.class);
                List<WithDraw> result = withDrawService.applyWithDrawOrBatch(list);
                Map<String,Object> map = new HashMap<>();
                map.put("flag",true);
                map.put("result",result);
                response = new Response(true,map);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else {
            response = new Response(false,"提现申请请求withdraws不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
        }

        return JSONObject.toJSONString(apiResponse);
    }

    @RequestMapping("/auditWithDraw")
    @ResponseBody
    public String auditWithDraw(String adminid,String withdraw){
        APIResponse apiResponse;
        Response response;
        if(!StringUtil.isEmpty(withdraw)){
            try{
                WithDraw withDraw = JSONObject.parseObject(withdraw,WithDraw.class);
                withDraw.setAdminid(adminid);
                withDraw.setAgreedatetime(StringUtil.currentDateTime());
                withDrawService.auditWithDraw(withDraw);
                response = new Response(true,withDraw.getAccountid() + "的提现请求更新结果成功" + withDraw);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else {
            response = new Response(false,"审核的提现请求不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
        }

        return JSONObject.toJSONString(apiResponse);
    }

    @RequestMapping("/queryWithDraws")
    @ResponseBody
    public String queryWithDraws(String adminid, @Nullable String withdrawinfo, @Nullable String startLine,@Nullable String offset){
        APIResponse apiResponse;
        Response response;
        try {
            Pager pager = null;
            if (!StringUtil.isEmpty(startLine) && !StringUtil.isEmpty(offset)) {
                pager = new Pager(Integer.parseInt(startLine), Integer.parseInt(offset));
            }

            WithDraw withDraw = null;
            if (!StringUtil.isEmpty(withdrawinfo)) {
                withDraw = JSONObject.parseObject(withdrawinfo, WithDraw.class);
            }

            List<WithDraw> list = withDrawService.queryWithDraws(withDraw, pager);

            response = new Response(true,list);
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            response = new Response(false,e.getMessage());
            apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return JSONObject.toJSONString(apiResponse);


    }
}
