package com.tibbers.zhonghui.intercepter;

import com.tibbers.zhonghui.annotation.TokenListen;
import com.tibbers.zhonghui.utils.CacheUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Tibbers
 * @create 2017-05-27 18:45
 */
//@TokenListen
public class TokenListenInterceptor extends HandlerInterceptorAdapter {
//    @Autowired
//    private CustInfoService custInfoService;

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
        String token = request.getParameter("token");
//        if (!StringUtil.isEmpty(token) && custInfoService.getCustInfoByToken(token) != null) {
//            return super.preHandle(request, response, handler);
//        } else {
//            response.setCharacterEncoding("UTF-8");
//            response.setContentType("text/html");
//            response.setDateHeader("Expires", 0);
//            response.setHeader("unlogin", "unlogin");
            return true;
//        }
    }
}