package com.tibbers.zhonghui.intercepter;

import com.alibaba.fastjson.JSONObject;
import com.tibbers.zhonghui.annotation.TokenListen;
import com.tibbers.zhonghui.config.AppConstants;
import com.tibbers.zhonghui.model.Administrator;
import com.tibbers.zhonghui.model.ReceiveAddress;
import com.tibbers.zhonghui.model.common.APIResponse;
import com.tibbers.zhonghui.model.common.Response;
import com.tibbers.zhonghui.utils.CacheUtil;
import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Date;

/**
 * @author Tibbers
 * @create 2017-05-27 18:45
 */
@TokenListen
public class TokenListenInterceptor extends HandlerInterceptorAdapter {
//    @Autowired
//    private CustInfoService custInfoService;
    private Logger logger = Logger.getLogger(getClass());
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try{
            TokenListen tokenListenType = ((HandlerMethod) handler).getBeanType().getAnnotation(TokenListen.class);
            TokenListen tokenListenMethod = ((HandlerMethod) handler).getMethodAnnotation(TokenListen.class);
            if (tokenListenType == null || tokenListenType.isListen() == false) {//类无该注解或注解为false
                if (tokenListenMethod == null || tokenListenMethod.isListen() == false) {//方法无该注释或注解为false
                    return super.preHandle(request, response, handler);
                } else {
                    return this.isInvalid(request, response, handler);
                }
            } else {//类有该注解
                if (tokenListenMethod != null && tokenListenMethod.isListen() == false) {//方法注解为false
                    return super.preHandle(request, response, handler);
                } else {
                    return this.isInvalid(request, response, handler);
                }

            }
        }catch (ClassCastException e){
            return super.preHandle(request, response, handler);//责任链，传递给下一个符合的Handler
        }

    }

    public boolean isInvalid(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String actionAdminid = request.getParameter("actionAdminid");
        boolean flag = false;
        try {
            Administrator administrator = (Administrator) CacheUtil.get(actionAdminid);
            if(administrator != null){
                CacheUtil.set(administrator.getAdminid(),administrator,new Date(1000 * 60 * 30));
                flag = true;
                super.preHandle(request,response,handler);
            }else {
                flag = false;
            }

        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        if(!flag){
            response.setContentType("application/json;charset=utf-8");//指定返回的格式为JSON格式
            response.setCharacterEncoding("UTF-8");//setContentType与setCharacterEncoding的顺序不能调换，否则还是无法解决中文乱码的问题
            Response res = new Response(false,"获取登录状态异常或者登录状态超时，请重新登录");
            APIResponse apiResponse = new APIResponse(AppConstants.RESPONSE_FAILED_CODE,AppConstants.REQUEST_STATUS_MESSAGE,res);
            PrintWriter out = response.getWriter();
            out.write(JSONObject.toJSONString(apiResponse));
            out.close();
        }

        return flag;

//        String token = request.getParameter("token");
//        if (!StringUtil.isEmpty(token) && custInfoService.getCustInfoByToken(token) != null) {
//            return super.preHandle(request, response, handler);
//        } else {
//            response.setCharacterEncoding("UTF-8");
//            response.setContentType("text/html");
//            response.setDateHeader("Expires", 0);
//            response.setHeader("unlogin", "unlogin");
//            return true;
//        }
    }
}