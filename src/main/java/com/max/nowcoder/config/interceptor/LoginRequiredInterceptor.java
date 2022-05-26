package com.max.nowcoder.config.interceptor;

import com.max.nowcoder.annotation.LoginRequired;
import com.max.nowcoder.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @program: nowcoder
 * @description:
 * @author: max-qaq
 * @create: 2022-05-26 10:10
 **/
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    HostHolder hostHolder;

    //访问之前判断是否登录
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod){
            //如果拦截的是方法才判定
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            //取注解
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
            if (loginRequired != null && hostHolder.getUser() == null){
                //需要登录并且没有用户
                //重定向
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            }
        }



        return true;
    }
}
