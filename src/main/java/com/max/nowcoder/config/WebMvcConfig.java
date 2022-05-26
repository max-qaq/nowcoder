package com.max.nowcoder.config;

import com.max.nowcoder.config.interceptor.LoginRequiredInterceptor;
import com.max.nowcoder.config.interceptor.LoginTicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: nowcoder
 * @description:
 * @author: max-qaq
 * @create: 2022-05-25 19:16
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    LoginTicketInterceptor loginTicketInterceptor;

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg");
        registry.addInterceptor(loginRequiredInterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg");
    }
}
