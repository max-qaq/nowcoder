package com.max.nowcoder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.max.nowcoder.dao.LoginTicketMapper;
import com.max.nowcoder.dao.UserMapper;
import com.max.nowcoder.entity.LoginTicket;
import com.max.nowcoder.entity.User;
import com.max.nowcoder.service.UserService;
import com.max.nowcoder.utils.CommunityConstant;
import com.max.nowcoder.utils.CommunityUtil;
import com.max.nowcoder.utils.HostHolder;
import com.max.nowcoder.utils.MyMailSender;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @program: nowcoder
 * @description:
 * @author: max-qaq
 * @create: 2022-05-18 19:32
 **/

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService , CommunityConstant {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MyMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private HostHolder hostHolder;
    //域名
    @Value("${community.path.domain}")
    private String domain;

    //项目名
    @Value("${server.servlet.context-path}")
    private String contextPath;


    /**
     * 注册功能
     * @param user
     * @return
     * @throws MessagingException
     */
    @Override
    public Map<String, Object> register(User user) throws MessagingException {
        Map<String, Object> map = new HashMap<>();
        //空值处理
        if (null == user){
            throw new IllegalArgumentException("参数不为空");
        }
        if (StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg","账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg","密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())){
            map.put("emailMsg","邮箱不能为空");
            return map;
        }
        //验证账号
        User userFromDataBase = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername,user.getUsername()));
        if (userFromDataBase != null){
            map.put("usernameMsg","该账号已存在");
            return map;
        }
        //验证邮箱
        userFromDataBase = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail,user.getEmail()));
        if (userFromDataBase != null){
            map.put("emailMsg","该邮箱已被注册");
            return map;
        }
        //注册用户
        //生成盐,5位
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID()); //激活码
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insert(user);

        //发送激活邮件
        user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getEmail,user.getEmail()));
        Context context = new Context();
        context.setVariable("email",user.getEmail());
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url",url);
        String content = templateEngine.process("/mail/activation",context);
        mailSender.sendMail(user.getEmail(),"激活账号",content);

        return map;
    }

    /**
     * 激活用户
     * @param userId
     * @param code
     * @return
     */
    @Override
    public int activation(int userId, String code){
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1){
            return ACTIVATION_REPEAT;
        }else if (user.getActivationCode().equals(code)){
            //激活账号
            user.setStatus(1);
            userMapper.updateById(user);
            //返回激活成功
            return ACTIVATION_SUCCESS;
        }else {
            return ACTIVATION_FAIL;
        }
    }

    @Override
    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();
        //空值处理
        if (StringUtils.isBlank(username)){
            map.put("usernameMsg","账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)){
            map.put("passwordMsg","密码不能为空");
            return map;
        }
        //验证账号
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user == null){
            map.put("usernameMsg","该账号不存在");
            return map;
        }
        //验证状态
        if (user.getStatus() == 0){
            map.put("usernameMsg","该账号未激活");
            return map;
        }
        //验证密码
        password = CommunityUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)){
            map.put("passwordMsg","密码不正确");
            return map;
        }
        //生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));

        loginTicketMapper.insert(loginTicket);
        map.put("ticket",loginTicket.getTicket());
        return map;
    }

    @Override
    public void logout(String ticket) {
        loginTicketMapper.updateStatus(ticket,1);
    }

    //更新头像url
    @Override
    public int updateHeader(int userId, String headerUrl) {
        User user = userMapper.selectById(userId);
        user.setHeaderUrl(headerUrl);
        return userMapper.updateById(user);
    }

    @Override
    public Map<String, Object> updatePassword(String oldPassword, String newPassword, Model model) {
        User user = hostHolder.getUser();
        Map<String,Object> map = new HashMap<>();
        oldPassword = CommunityUtil.md5(oldPassword + user.getSalt());
        if (oldPassword.equals(user.getPassword())){
            //密码相等，更改密码
            newPassword = CommunityUtil.md5(newPassword + user.getSalt());
            user.setPassword(newPassword);
            userMapper.updateById(user);
        }
        //不等，返回错误信息
        else{
            map.put("oldPasswordMsg","原密码错误！");
        }

        return map;
    }
}
