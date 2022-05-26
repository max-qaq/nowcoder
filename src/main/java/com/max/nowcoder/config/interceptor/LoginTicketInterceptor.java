package com.max.nowcoder.config.interceptor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.max.nowcoder.dao.LoginTicketMapper;
import com.max.nowcoder.dao.UserMapper;
import com.max.nowcoder.entity.LoginTicket;
import com.max.nowcoder.entity.User;
import com.max.nowcoder.utils.CookieUtil;
import com.max.nowcoder.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @program: nowcoder
 * @description:登录拦截器
 * @author: max-qaq
 * @create: 2022-05-25 18:40
 **/
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Autowired
    LoginTicketMapper loginTicketMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    HostHolder hostHolder;

    //请求之前获取用户
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        从cookie中获取ticket
        String ticket = CookieUtil.getValue(request, "ticket");

        if (ticket != null){
            //查询凭证
            LoginTicket loginTicket = loginTicketMapper.selectOne(new LambdaQueryWrapper<LoginTicket>().eq(LoginTicket::getTicket, ticket));
            //检查凭证是否有效
            if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())){
                //获取用户
                User user = userMapper.selectById(loginTicket.getUserId());
                //在本次请求中持有用户
                hostHolder.setUser(user);
            }
        }
        return true;
    }

    //在模板引擎调用之前注入到model里面
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null){
            modelAndView.addObject("loginUser",user);
        }
    }

    //模板引擎执行结束清理用户
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
