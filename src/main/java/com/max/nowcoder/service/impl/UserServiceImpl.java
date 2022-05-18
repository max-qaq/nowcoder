package com.max.nowcoder.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.max.nowcoder.dao.UserMapper;
import com.max.nowcoder.entity.User;
import com.max.nowcoder.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @program: nowcoder
 * @description:
 * @author: max-qaq
 * @create: 2022-05-18 19:32
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
}
