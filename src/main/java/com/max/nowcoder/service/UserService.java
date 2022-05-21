package com.max.nowcoder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.max.nowcoder.entity.User;

import javax.mail.MessagingException;
import java.util.Map;

public interface UserService extends IService<User> {
    public Map<String , Object> register(User user) throws MessagingException;
}
