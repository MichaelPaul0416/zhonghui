package com.tibbers.zhonghui.controller;

import com.alibaba.fastjson.JSONObject;
import com.tibbers.zhonghui.config.APIException;
import com.tibbers.zhonghui.config.AppConstants;
import com.tibbers.zhonghui.model.SysParam;
import com.tibbers.zhonghui.model.common.APIResponse;
import com.tibbers.zhonghui.model.common.Response;
import com.tibbers.zhonghui.service.ISysParamService;
import com.tibbers.zhonghui.utils.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/20 13:48
 * @description:
 */
@Controller
@RequestMapping("sysparam")
public class SysParamController {

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private ISysParamService iSysParamService;

    @RequestMapping("/queryCategoryCaption")
    @ResponseBody
    public String queryCategoryCaption(){
        APIResponse apiResponse;
        Response response;
        try{
            List<Map<String,String>> results = iSysParamService.queryAllType();
            response = new Response(true,results);
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            response = new Response(false,e.getCause().getMessage());
            apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return JSONObject.toJSONString(apiResponse);
    }

    @RequestMapping("/createSysCategory")
    @ResponseBody
    public String createSysCategory(HttpServletRequest request){
        logger.info("收到创建系统配置大类参数请求");
        String itemName = request.getParameter("itemName");
        String caption = request.getParameter("caption");
        String description = request.getParameter("description");
        String reverse = request.getParameter("reverse");

        SysParam sysParam = new SysParam();
        sysParam.setCaption(caption);
        sysParam.setDescription(description);
        sysParam.setItemid("#");
        sysParam.setItemname(itemName);
        sysParam.setReverse(reverse);

        logger.info(String.format("发送给业务层的请求参数[%s]",sysParam));
        APIResponse apiResponse;
        try {
            iSysParamService.createSysCategory(sysParam);
            Response response = new Response(true, AppConstants.SERVICE_SUCCEED_MESSAGE);
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE, AppConstants.SERVICE_SUCCEED_MESSAGE, response);
        }catch (APIException e){
            logger.error(e.getMessage(),e);
            Response response = new Response(false,e.getMessage());
            apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }
        return String.valueOf(JSONObject.toJSON(apiResponse));
    }

    @RequestMapping("/createItem4Category")
    @ResponseBody
    public String createItem4Category(String itemList){
        APIResponse apiResponse ;
        Response response;
        if(!StringUtils.isEmpty(itemList)){
            List<SysParam> list = JSONObject.parseArray(itemList,SysParam.class);
            try {
                if (list != null && list.size() > 0) {
                    iSysParamService.createItem4Category(list);
                    response = new Response(true, AppConstants.SERVICE_SUCCEED_MESSAGE);
                    apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE, AppConstants.SERVICE_SUCCEED_MESSAGE, response);
                } else {
                    response = new Response(false, "没有接收到需要新增的系统Item配置项");
                    apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE, AppConstants.REQUEST_STATUS_MESSAGE, response);
                }
            }catch (APIException e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else{
            response = new Response(false, "参数itemList不能传入空字符串或该参数未传入");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE, AppConstants.REQUEST_STATUS_MESSAGE, response);
        }
        return String.valueOf(JSONObject.toJSON(apiResponse));
    }


    @RequestMapping("/queryConfigByCategoryId")
    @ResponseBody
    public String queryConfigByCategoryId(String categoryid){
        APIResponse apiResponse;
        Response response;
        try {
            List<SysParam> queryData;
            List<SysParam> result;
            if(StringUtils.isEmpty(categoryid)){
                result = iSysParamService.loadSysConfig2Cache();

            }else {
                result = iSysParamService.queryConfigByCategoryId(categoryid);
            }
            queryData = new ArrayList<>();
            for(SysParam sysParam : result){
                if(!"#".equals(sysParam.getItemid())){
                    queryData.add(sysParam);
                }
            }
            response = new Response(true,queryData);
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
        }catch (APIException e){
            logger.error(e.getMessage(),e);
            response = new Response(false,e.getMessage());
            apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }
        return String.valueOf(JSONObject.toJSON(apiResponse));
    }

    @RequestMapping("/deleteCategoryItem")
    @ResponseBody
    public String deleteCategoryItem(SysParam sysParam){
        APIResponse apiResponse;
        Response response;
        try{
            iSysParamService.deleteCategoryItem(sysParam);
            response = new Response(true,"删除明细配置成功");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
        }catch (APIException e){
            logger.error(e.getMessage(),e);
            response = new Response(false,"删除明细配置失败");
            apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }
        return String.valueOf(JSONObject.toJSON(apiResponse));
    }

    @RequestMapping("/updateCategoryItem")
    @ResponseBody
    public String updateCategoryItem(SysParam sysParam){
        APIResponse apiResponse;
        Response response;
        try{
            iSysParamService.updateCategoryItem(sysParam);
            response = new Response(true,"修改明细配置成功");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
        }catch (APIException e){
            logger.error(e.getMessage(),e);
            response = new Response(false,"修改明细配置失败");
            apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }
        return String.valueOf(JSONObject.toJSON(apiResponse));
    }
}
