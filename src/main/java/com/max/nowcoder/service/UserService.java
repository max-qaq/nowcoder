package com.max.nowcoder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.max.nowcoder.entity.User;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Map;

public interface UserService extends IService<User> {
    public Map<String , Object> register(User user) throws MessagingException;
    public int activation(int userId, String code);

    //登录
    public Map<String,Object> login(String username, String password, int expiredSeconds);

    //退出
    public void logout(String ticket);

}
