package com.tibbers.zhonghui.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import com.tibbers.zhonghui.config.APIException;
import com.tibbers.zhonghui.config.AppConstants;
import com.tibbers.zhonghui.config.ServiceConfigBean;
import com.tibbers.zhonghui.model.Account;
import com.tibbers.zhonghui.model.common.APIResponse;
import com.tibbers.zhonghui.model.common.Pager;
import com.tibbers.zhonghui.model.common.Response;
import com.tibbers.zhonghui.service.IAccountService;
import com.tibbers.zhonghui.service.IRecommandService;
import com.tibbers.zhonghui.utils.StringUtil;
import com.tibbers.zhonghui.utils.WxLoginUtil;
import org.apache.log4j.Logger;
import org.springframework.aop.framework.AbstractAdvisingBeanPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Paul
 * @time:2018/1/21 13:06
 * @description:
 */
@Controller
@RequestMapping("accountservice")
public class AccountServiceController {

    private Logger logger = Logger.getLogger(getClass());


    @Autowired
    private IAccountService accountService;

    @Autowired
    private IRecommandService recommandService;

    @Autowired
    private ServiceConfigBean serviceConfigBean;


    @RequestMapping("/checkInfoComplete")
    @ResponseBody
    public String checkInfoComplete(String code){
        APIResponse apiResponse;
        Response response;

        if(StringUtil.isEmpty(code)){
            response = new Response(false,"code不能为空");
            apiResponse = new APIResponse(AppConstants.REQUEST_STATUS_MESSAGE,AppConstants.RESPONSE_FAILED_CODE,response);
        }else {
            try{
                boolean complete = accountService.checkInfoComplete(code);
                response = new Response(false,complete);
                apiResponse = new APIResponse(AppConstants.REQUEST_STATUS_MESSAGE,AppConstants.RESPONSE_SUCCEED_CODE,response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getCause().getMessage());
                apiResponse = new APIResponse(AppConstants.REQUEST_STATUS_MESSAGE,AppConstants.RESPONSE_FAILED_CODE,response);
            }
        }

        return JSONObject.toJSONString(apiResponse);
    }


    @RequestMapping("/accountCodeID")
    @ResponseBody
    public void accountCodeID(HttpServletRequest request,HttpServletResponse httpServletResponse){

        File codeImage = null;
        String accountid = request.getParameter("accountid");
//        String access_token = request.getParameter("access_token");
        FileInputStream inputStream = null;
        OutputStream outputStream;
        String info = null;
        try {
            if(!StringUtils.isEmpty(accountid)){
                Account account = accountService.queryByAccountid(accountid);
                if(account != null){
                    if("1".equals(account.getIsvip())) {
                        String codePath = account.getCodeImagepath();
                        if (!StringUtil.isEmpty(codePath)) {
                            codeImage = new File(codePath);
                        } else {
                            String path = WxLoginUtil.landAccountCodeImage(accountid, serviceConfigBean.getWxenvcodeImagePathPrefix());
                            Account update = new Account();
                            update.setAccountid(accountid);
                            update.setCodeImagepath(path);
                            accountService.updateAccountInfo(new Gson().toJson(update));
                            logger.info(String.format("账户[%s]的场景二维码存储成功[%s]", accountid, path));
                            codeImage = new File(path);
                        }
                        if (!codeImage.exists()) {
                            codeImage = null;
                            info = "场景二维码尚未找到，请联系管理员";
                        }
                    }else {
                        info = "您当前不是VIP用户，如果想要使用推荐功能，请联系管理员为您升级为VIP用户";
                    }
                }else {
                    codeImage = null;
                    info = String.format("当前用户[%s]不存在，请联系管理员",accountid);
                }
            }else{
                codeImage = null;
                info = "需要查询的账户编号accountid不能为空";
            }
            byte[] data;
            if(codeImage != null) {
                inputStream = new FileInputStream(codeImage);
                data = new byte[(int) codeImage.length()];
                inputStream.read(data);
                httpServletResponse.setContentType("image/png");
            }else {
                data = info.getBytes();
            }
            outputStream = httpServletResponse.getOutputStream();
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        }catch (IOException e) {
            logger.error(e.getMessage(),e);
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(),e);
                }
            }
        }
    }

    @RequestMapping("/recommandByVip")
    @ResponseBody
    public String queryByAccountid(String accountid){
        APIResponse apiResponse;
        Response response;
        if(!StringUtil.isEmpty(accountid)){
            try {
                logger.info(String.format("开始查询账户编号[%s]是否被vip推荐", accountid));
                Map<String,String> result = recommandService.recommandByVIP(accountid);
                logger.info(String.format("查询到账户编号[%s]是否被vip推荐结果", accountid, result));
                if(result != null && !StringUtil.isEmpty(result.get("isvip"))){
                    if("1".equals(result.get("isvip"))){
                        result.put("isvip","true");
                    }else {
                        result.put("recommander","");
                        result.put("isvip","false");
                    }
                }else {
                    throw new APIException("用户["+accountid+"]不存在");
                }
                response = new Response(true, result);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE, AppConstants.SERVICE_SUCCEED_MESSAGE, response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response( false,e.getCause().getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }
        }else {
            response = new Response(false,"查询账户编号accountid不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
        }

        return JSONObject.toJSONString(apiResponse);
    }

    @RequestMapping("/queryAccountByOpenid")
    @ResponseBody
    public String queryAccountByOpenid(String openid,String imageUrl){
        APIResponse apiResponse ;
        Response response;

        if(StringUtil.argsNotEmpty(new String[]{openid,imageUrl})){
            try{
                Account account = accountService.queryAccountByOpenid(openid);
                Map<String,Object> result = new HashMap<>();
                if(account != null && !StringUtil.isEmpty(account.getAccountid())){
                    result.put("registry",true);
                    result.put("msg","已注册");
                    result.put("accountid",account.getAccountid());
                    if(!StringUtil.isEmpty(imageUrl) && !imageUrl.equals(account.getImagepath())){
                        Account update = new Account();
                        update.setAccountid(account.getAccountid());
                        update.setImagepath(imageUrl);
                        logger.info(String.format("更新用户[%s]的微信头像信息[%s]",account.getAccountid(),imageUrl));
                        accountService.updateAccountInfo(new Gson().toJson(update));
                    }
                    result.put("imagePath",imageUrl);
                }else {
                    result.put("registry",false);
                    result.put("msg",openid + "对应的微信用户未在本系统中注册绑定");
                }
                response = new Response(true,result);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getCause().getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else {
            response = new Response(false,"微信openid,微信头像imageUrl不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }
        return JSONObject.toJSONString(apiResponse);
    }

    @RequestMapping("/queryScore")
    @ResponseBody
    public String queryScore(String accountid){
        APIResponse apiResponse;
        Response response;
        try {
            Account account = null;
            if (!StringUtil.isEmpty(accountid)) {
                account = new Account();
                account.setAccountid(accountid);
            }
            Map<String, Object> param = new HashMap<>();
            param.put("account", account);
            List<Map<String, Object>> ranks = accountService.orderByScore(param);
            logger.info(String.format("查询到的排名信息[%s]", ranks));

            response = new Response(true,ranks);
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            response = new Response(false,e.getCause().getMessage());
            apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return JSONObject.toJSONString(apiResponse);

    }

    @RequestMapping("/registerAccount")
    @ResponseBody
    public String registerAccount(String personInfo,String accountInfo){
        logger.info("接收到注册用户请求");
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("personInfo",personInfo);
        paramMap.put("accountInfo",accountInfo);

        APIResponse apiResponse;
        Response response;
        Map<String, String> registResult;
        try {
            registResult = accountService.registerNewAccount(paramMap);
            response = new Response(true,registResult);
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            response = new Response(false,e.getMessage());
            apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }
        return String.valueOf(JSONObject.toJSON(apiResponse));
    }

    @RequestMapping("/updateAccountInfo")
    @ResponseBody
    public String updateAccountInfo(String accountinfo){
        APIResponse apiResponse;
        Response response;
        if(!StringUtils.isEmpty(accountinfo)){
            try{
                accountService.updateAccountInfo(accountinfo);
                response = new Response(true,"账户信息更新成功");
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else {
            response = new Response(false,"修改账户信息为空，请重新输入");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }
        return String.valueOf(JSONObject.toJSON(apiResponse));
    }

    @RequestMapping("/uploadAccountImage")
    @ResponseBody
    public String uploadAccountImage(HttpServletRequest servletRequest){
        APIResponse apiResponse;
        Response response;
        String accountid = servletRequest.getParameter("accountid");

        if(!StringUtil.isEmpty(accountid)){
            try{
                List<String> paths = accountService.uploadAccountImage(servletRequest);
                Map<String,Object> map = new HashMap<>();
                map.put("upload",true);
                map.put("paths",paths);
                map.put("size",paths.size());
                map.put("msg","upload successfully");

                response = new Response(true,map);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getCause().getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else {
            response = new Response(false,"账户编号accountid不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
        }

        return JSONObject.toJSONString(apiResponse);
    }


    @RequestMapping("/updatePersonalInfo")
    @ResponseBody
    public String updatePersonalInfo(String personinfo){
        APIResponse apiResponse;
        Response response;

        if(!StringUtils.isEmpty(personinfo)){
            try {
                accountService.updatePersonalInfo(personinfo);
                response = new Response(true,"更新个人信息成功");
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else {
            response = new Response(false,"输入更新的个人信息为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return String.valueOf(JSONObject.toJSON(apiResponse));
    }

    @RequestMapping("/showAccountImage")
    public void showAccountImage(String accountid, HttpServletResponse response){
        File accountImage;
        if(!StringUtils.isEmpty(accountid)){
            Account account = accountService.queryByAccountid(accountid);
            if(account != null){
                accountImage = new File(account.getImagepath());
                if(!accountImage.exists()){
                    accountImage = new File(serviceConfigBean.getDefaultImagePath());
                }
            }else {
                accountImage = new File(serviceConfigBean.getDefaultImagePath());
            }
        }else{
            accountImage = new File(serviceConfigBean.getDefaultImagePath());
        }
        FileInputStream inputStream = null;
        OutputStream outputStream;
        try {
            inputStream = new FileInputStream(accountImage);
            byte[] data = new byte[(int)accountImage.length()];
            inputStream.read(data);
            response.setContentType("image/jpeg");
            outputStream = response.getOutputStream();
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        }catch (IOException e) {
            logger.error(e.getMessage(),e);
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(),e);
                }
            }
        }
    }

    @RequestMapping("/queryAccountTradeDetails")
    @ResponseBody
    public String queryAccountTradeDetails(String accountid,  String startLine,  String offset){
        APIResponse apiResponse ;
        Response response;
        if(!StringUtil.isEmpty(accountid)){
            try{
                Pager pager = null;
                if(!StringUtil.isEmpty(startLine) && !StringUtil.isEmpty(offset)){
                    pager = new Pager(Integer.parseInt(startLine),Integer.parseInt(offset));
                }
                Map<String,List<Map<String,Object>>> results = accountService.queryAccountTrades(accountid,pager);
                logger.info(String.format("查询到账户[%s]的资金收支明细",accountid));
                response = new Response(true,results);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getCause().getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else {
            response = new Response(false,"查询账户id不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return JSONObject.toJSONString(apiResponse);
    }

    @RequestMapping("/adminQueryAccounts")
    @ResponseBody
    public String adminQueryAccounts(String isvip,String termid, String startLine, String offset){
        APIResponse apiResponse ;
        Response response;

        if(StringUtil.argsNotEmpty(new String[]{isvip})){
            try{
                Pager pager = null;
                if(!StringUtil.isEmpty(startLine) && !StringUtil.isEmpty(offset)){
                    pager = new Pager(Integer.parseInt(startLine),Integer.parseInt(offset));
                }
                List<Account> accounts = accountService.queryAccounts(isvip, termid, pager);
                response = new Response(true,accounts);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getCause().getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else {
            response = new Response(false,"查询的账户是否vip");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return JSONObject.toJSONString(apiResponse);
    }

    @RequestMapping("/queryWxOpenid")
    @ResponseBody
    public String queryWxOpneid(String code){
        APIResponse apiResponse;
        Response response;
        if(!StringUtil.isEmpty(code)){

            logger.info(String.format("根据code[%s]从微信获取openid",code));
            try {
                JSONObject object = WxLoginUtil.doLoginAuth(code);
                logger.info(String.format("微信返回信息[%s]", object));
                String openid = (String) object.get("openid");
                if (StringUtil.isEmpty(openid)) {
                    throw new APIException("微信返回的openid为空，请检查code的时效性或者联系管理员");
                }
                Map<String, String> data = new HashMap<>();
                data.put("openid", openid);
                data.put("code", "code");
                response = new Response(true, data);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE, AppConstants.SERVICE_SUCCEED_MESSAGE, response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getCause().getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else {
            response = new Response(false,"换取openid的code不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
        }

        return JSONObject.toJSONString(apiResponse);
    }
}
