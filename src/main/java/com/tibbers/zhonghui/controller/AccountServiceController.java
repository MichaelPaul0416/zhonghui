package com.tibbers.zhonghui.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.sun.istack.internal.Nullable;
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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
                    if(!imageUrl.equals(account.getImagepath())){
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
    public String queryScore(@Nullable String accountid){
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
    public String uploadAccountImage(@RequestParam("upload") MultipartFile file, HttpServletRequest request){
        APIResponse apiResponse;
        Response response;

        try {
            if(file.getInputStream() != null){
                String accountid = request.getParameter("accountid");
                Account account = new Account();
                account.setAccountid(accountid);
                accountService.uploadAccountImage(file,account);
                response = new Response(true,String.format("文件[%s]落地成功",file.getOriginalFilename()));
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }else{
                response = new Response(false,"读取的文件为空，请先选择有效的文件");
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
            response = new Response(false,"读取文件流失败,确认文件是否正确");
            apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            response = new Response(false,e.getMessage());
            apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return String.valueOf(JSONObject.toJSON(apiResponse));
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
    public String queryAccountTradeDetails(String accountid, @Nullable String startLine, @Nullable String offset){
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
    public String adminQueryAccounts(String isvip,String termid,@Nullable String startLine,@Nullable String offset){
        APIResponse apiResponse ;
        Response response;

        if(StringUtil.argsNotEmpty(new String[]{isvip,termid})){
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
            response = new Response(false,"查询的账户是否vip，账户类型termid不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return JSONObject.toJSONString(apiResponse);
    }
}
