package com.tibbers.zhonghui.controller;

import com.alibaba.fastjson.JSONObject;
import com.sun.istack.internal.Nullable;
import com.tibbers.zhonghui.config.APIException;
import com.tibbers.zhonghui.config.AppConstants;
import com.tibbers.zhonghui.model.common.APIResponse;
import com.tibbers.zhonghui.model.common.Pager;
import com.tibbers.zhonghui.model.common.Response;
import com.tibbers.zhonghui.service.IAuditingProsService;
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
 * @time:2018/1/21 23:28
 * @description:
 */
@Controller
@RequestMapping("auditPros")
public class AuditingProsController {

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private IAuditingProsService auditingProsService;

    @RequestMapping("/applyAudit4Product")
    @ResponseBody
    public String applyAudit4Product(String productid){
        APIResponse apiResponse;
        Response response;
        if(!StringUtils.isEmpty(productid)){
            try{
                auditingProsService.applyAudit4Product(productid);
                response = new Response(true,String.format("产品[%s]申请审核成功",productid));
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (APIException e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else{
            response = new Response(false,"需要申请审核的产品id不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return String.valueOf(JSONObject.toJSON(apiResponse));
    }

    @RequestMapping("/auditProductApply")
    @ResponseBody
    public String auditProductApply(String serialid,String auditor,String auditstate){
        APIResponse apiResponse ;
        Response response;
        if(!StringUtil.isEmpty(serialid) && !StringUtil.isEmpty(auditor) && !StringUtil.isEmpty(auditstate)){
            try{
                auditingProsService.auditProductApply(auditor,auditstate,serialid);
                response = new Response(true,"修改审核状态成功");
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (APIException e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else{
            response = new Response(false,"审核流水id,审核员,审核状态必须传值，不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return String.valueOf(JSONObject.toJSON(apiResponse));
    }

    @RequestMapping("/querySerialByAuditState")
    @ResponseBody
    public String querySerialByAuditState(String auditstate, @Nullable String startLine,@Nullable String offset){
        APIResponse apiResponse;
        Response response ;
        if(!StringUtil.isEmpty(auditstate)){
            try{
                Pager pager = null;
                if(!StringUtils.isEmpty(startLine) && !StringUtils.isEmpty(offset)){
                    pager = new Pager(Integer.parseInt(startLine),Integer.parseInt(offset));
                }
                List<Map<String,String>> list = auditingProsService.querySerialByAuditState(auditstate,pager);
                response = new Response(true,list);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (APIException e){
                response = new Response(false,e.getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else {
            response = new Response(false,"请选择查询的审核状态[0未审核，1审核通过，2审核未通过]");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return String.valueOf(JSONObject.toJSON(apiResponse));
    }
}
