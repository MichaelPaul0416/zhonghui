package com.tibbers.zhonghui.controller;

import com.alibaba.fastjson.JSONObject;
import com.sun.istack.internal.Nullable;
import com.tibbers.zhonghui.config.APIException;
import com.tibbers.zhonghui.config.AppConstants;
import com.tibbers.zhonghui.model.Recommand;
import com.tibbers.zhonghui.model.common.APIResponse;
import com.tibbers.zhonghui.model.common.Pager;
import com.tibbers.zhonghui.model.common.Response;
import com.tibbers.zhonghui.service.IRecommandService;
import com.tibbers.zhonghui.service.IWithDrawService;
import com.tibbers.zhonghui.utils.StringUtil;
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
 * @time:2018/1/23 0:56
 * @description:
 */
@Controller
@RequestMapping("recommandRelation")
public class RecommandRelationController {

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private IRecommandService recommandService;

    @Autowired
    private IWithDrawService withDrawService;

    @RequestMapping("/queryTotalIncomAndWithdraw")
    @ResponseBody
    public String queryTotalIncomAndWithdraw(String accountid){
        APIResponse apiResponse;
        Response response;

        if(!StringUtil.isEmpty(accountid)){
            try{
                Map<String,Object> map = new HashMap<>();
                List<Map<String,String>> income = recommandService.queryTotalRecommandIncome(accountid);
                if (income.size() == 1) {
                    map.put("income", income.get(0));
                }else {
                    map.put("income","");
                }
                List<Map<String,String>> withdraw = withDrawService.queryTotalWithdraw(accountid);
                if(withdraw.size() == 1) {
                    map.put("withdraw", withdraw.get(0));
                }else{
                    map.put("withdraw","");
                }
                response = new Response(true,map);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getCause().getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else {
            response = new Response( false,"账户编号accountid不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
        }

        return JSONObject.toJSONString(apiResponse);
    }

    @RequestMapping("/insertRecommand")
    @ResponseBody
    public String insertRecommand(String accountid,String recommand){//前者是被推荐者的账户id，后者是推荐者的账户id，此时被推荐者的信息已经录入到系统taccount中，所以已经拿到生成的accountid
        APIResponse apiResponse;
        Response response;
        if(!StringUtils.isEmpty(recommand)){
            try{
                recommandService.insertRecommand(accountid,recommand);
                response = new Response(true,"推荐人与被推荐人关系新增成功");
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (APIException e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else {
            response = new Response(false,"输入的推荐关系不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return String.valueOf(JSONObject.toJSON(apiResponse));
    }

    @RequestMapping("/queryRecommandByPager")
    @ResponseBody
    public String queryRecommandByPager(@Nullable String startLine,@Nullable String offset){
        APIResponse apiResponse;
        Response response;
        Pager pager = null;
        try {
            if (!StringUtils.isEmpty(startLine) && !StringUtils.isEmpty(offset)) {
                pager = new Pager(Integer.parseInt(startLine), Integer.parseInt(offset));
            }
            List<Recommand> list= recommandService.queryRecommandByPager(pager);
            response = new Response(true,list);
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            response = new Response(false,e.getMessage());
            apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return String.valueOf(JSONObject.toJSON(apiResponse));
    }

    @RequestMapping("/queryMyRecommandAccounts")
    @ResponseBody
    public String queryMyRecommandAccounts(String accountid,@Nullable String startLine,@Nullable String offset){
        APIResponse apiResponse;
        Response response;
        if(!StringUtil.isEmpty(accountid)){
            try{
                Pager pager = null;
                if(!StringUtil.isEmpty(startLine) && !StringUtil.isEmpty(offset)){
                    pager = new Pager(Integer.valueOf(startLine),Integer.valueOf(offset));
                }
                List<Map<String,String>> result = recommandService.queryMyRecommandAccounts(accountid,pager);
                response = new Response(true,result);
                apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.SERVICE_SUCCEED_MESSAGE,response);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                response = new Response(false,e.getCause().getMessage());
                apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
            }
        }else {
            response = new Response(false,"推荐人账户accountid不能为空");
            apiResponse = new APIResponse(AppConstants.RESPONSE_SUCCEED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,response);
        }

        return JSONObject.toJSONString(apiResponse);
    }
}
