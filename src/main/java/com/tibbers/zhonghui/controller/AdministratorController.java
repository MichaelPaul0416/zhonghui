package com.tibbers.zhonghui.controller;

import com.alibaba.fastjson.JSONObject;
import com.tibbers.zhonghui.annotation.TokenListen;
import com.tibbers.zhonghui.config.AppConstants;
import com.tibbers.zhonghui.model.Administrator;
import com.tibbers.zhonghui.model.common.APIResponse;
import com.tibbers.zhonghui.model.common.Response;
import com.tibbers.zhonghui.service.IAdministratorService;
import com.tibbers.zhonghui.utils.CacheUtil;
import com.tibbers.zhonghui.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/2/27 20:15
 * @description:
 */
@Controller
@RequestMapping("admin")
public class AdministratorController {

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private IAdministratorService administratorService;

    @TokenListen
    @RequestMapping("/createAdmin")
    @ResponseBody
    public String createAdmin(String actionAdminid,String adminInfo){
        APIResponse apiResponse;
        Response response;
        if(!StringUtil.argsEmpty(new String[]{actionAdminid,adminInfo})){
            try {
                String adminid = administratorService.createSingleAdmin(adminInfo);
                Map<String,String> map = new HashMap<>();
                map.put("adminid",adminid);
                response = new Response(true,map);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getCause().getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }

        }else {
            response = new Response(false,"操作员actionAdminid,管理员信息不准为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return JSONObject.toJSONString(apiResponse);
    }

    @TokenListen
    @RequestMapping("/deleteAdmin")
    @ResponseBody
    public String deleteAdmin(String actionAdminid,String adminid){
        APIResponse apiResponse;
        Response response;
        if(StringUtil.argsEmpty(new String[]{actionAdminid,adminid})) {
            try{
                administratorService.deleteAdmin(adminid);
                logger.warn(String.format("已经删除管理员[%d]的角色",adminid));
                response = new Response(true,"管理员id" + adminid +"删除成功");
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getCause().getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else {
            response = new Response(false,"actionAdminid,adminid不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
        }

        return JSONObject.toJSONString(apiResponse);
    }

    @TokenListen
    @RequestMapping("/updateAdminInfo")
    @ResponseBody
    public String updateAdminInfo(String actionAdminid,String updateInfo){
        APIResponse apiResponse;
        Response response;

        if(StringUtil.argsEmpty(new String[]{actionAdminid,updateInfo})){
            try{
                administratorService.updateInfoAdmin(updateInfo);
                response = new Response(true,"管理员信息更新成功");
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getCause().getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else {
            response = new Response(false,"actionAdminid,更新的管理员信息不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
        }

        return JSONObject.toJSONString(apiResponse);
    }

    @TokenListen
    @RequestMapping("/queryAllAdmins")
    @ResponseBody
    public String queryAllAdmins(String actionAdminid){
        APIResponse apiResponse ;
        Response response;
        if(StringUtils.isNoneEmpty(actionAdminid)) {
            try {
                Map<String, List<Administrator>> map = new HashMap<>();
                List<Administrator> list = administratorService.queryAdmins();
                for(Administrator admin : list){
                    admin.setAdminpassword("");
                }
                map.put("all", list);
                response = new Response(true, map);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE, AppConstants.SERVICE_SUCCEED_MESSAGE, response);

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                response = new Response(false, e.getCause().getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE, AppConstants.REQUEST_STATUS_MESSAGE, response);
            }
        }else {
            response = new Response(false,"操作员actionAdminid不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return JSONObject.toJSONString(apiResponse);
    }

    @TokenListen
    @RequestMapping("/queryByAdminid")
    @ResponseBody
    public String queryByadminid(String actionAdminid,String adminid){
        APIResponse apiResponse;
        Response response;

        if(StringUtil.argsEmpty(new String[]{actionAdminid,adminid})){
            try{
                Administrator administrator = new Administrator();
                administrator.setAdminid(adminid);
                administrator = administratorService.queryAdmin(administrator);
                administrator.setAdminpassword("");
                Map<String,Administrator> map = new HashMap<>();
                map.put("admin",administrator);
                response = new Response(true,map);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getCause().getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else {
            response = new Response(false,"actionAdminid,adminid不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
        }

        return JSONObject.toJSONString(apiResponse);
    }

    @RequestMapping("/adminLogin")
    @ResponseBody
    public String adminLogin(String adminname,String password){
        APIResponse apiResponse ;
        Response response;

        if(StringUtil.argsEmpty(new String[]{adminname,password})){
            try{
                Administrator administrator = new Administrator();
                administrator.setAdminname(adminname);
                administrator.setAdminpassword(password);
                if(administratorService.loginAdmin(administrator)){
                    administrator = administratorService.queryAdmin(administrator);
                    CacheUtil.set(administrator.getAdminid(),administrator,new Date(1000 * 60 *30));
                    logger.info(String.format("管理员[%s]已经登录，状态添加到缓存，有效期三十分钟",administrator));
                    Map<String,String> map = new HashMap<>();
                    map.put("adminid",administrator.getAdminid());
                    map.put("adminname",administrator.getAdminname());
                    response = new Response(true,map);
                    apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
                }else {
                    response = new Response(false,"账号密码不匹配，请重新登录");
                    apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
                }
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getCause().getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else {
            response = new Response(false,"登录账户信息不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
        }

        return JSONObject.toJSONString(apiResponse);
    }
}
