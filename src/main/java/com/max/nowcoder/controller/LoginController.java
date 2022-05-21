package com.max.nowcoder.controller;

import com.max.nowcoder.entity.User;
import com.max.nowcoder.service.UserService;
import com.max.nowcoder.utils.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;
import java.util.Map;

/**
 * @program: nowcoder
 * @description:
 * @author: max-qaq
 * @create: 2022-05-20 11:19
 **/
@Controller
public class LoginController implements CommunityConstant {

    @Autowired
    UserService userService;

    @GetMapping("/register")
    public String getRegisterPage(){
        return "/site/register";
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "/site/login";
    }

    @PostMapping("/register")
    public String register(Model model, User user) throws MessagingException {
        Map<String, Object> register = userService.register(user);
        if (register == null ||register.isEmpty()){
            //第一次注册,进行页面跳转
            model.addAttribute("msg","注册成功！我们已经向您的邮箱发送了一封激活邮件，请尽快激活");
            model.addAttribute("target","/index");
            return "/site/operate-result";
        }else{
            //注册过，返回三种可能的错误，给前端去处理
            model.addAttribute("usernameMsg",register.get("usernameMsg"));
            model.addAttribute("passwordMsg",register.get("passwordMsg"));
            model.addAttribute("emailMsg",register.get("emailMsg"));
            return "/site/register";
        }
    }

    @GetMapping("/activation/{userId}/{code}")
    public String activation(Model model, @PathVariable int userId, @PathVariable String code){
        int result = userService.activation(userId, code);
        if (result == ACTIVATION_SUCCESS){
            model.addAttribute("msg","激活成功！您的账号已经可以正常使用了");
            model.addAttribute("target","/login");
        }else if (result == ACTIVATION_REPEAT){
            model.addAttribute("msg","操作无效！该账号已经激活过了");
            model.addAttribute("target","/index");
        }else{
            model.addAttribute("msg","激活失败！激活码不正确");
            model.addAttribute("target","/index");
        }
        return "/site/operate-result";
    }

}
