package com.max.nowcoder.controller;

import com.google.code.kaptcha.Producer;
import com.max.nowcoder.entity.User;
import com.max.nowcoder.service.UserService;
import com.max.nowcoder.utils.CommunityConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
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

    @Autowired
    Producer kaptchaProducer;

    //项目名
    @Value("${server.servlet.context-path}")
    private String contextPath;

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

    /**
     * 获取验证码
     */
    @GetMapping("/kaptcha")
    public void getKaptcha(HttpServletResponse response, HttpSession session) throws IOException {
        //生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);
        //验证码存入session
        session.setAttribute("Kaptcha",text);
        //图片输出给浏览器
        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        ImageIO.write(image,"png",os);
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public String login(String username, String password, String code, boolean rememberme,
                        Model model,HttpSession session,HttpServletResponse response){
        //检查验证码
        String kaptcha = (String) session.getAttribute("Kaptcha");
        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)){
            //验证码不通过
            model.addAttribute("codeMsg","验证码不正确");
            return "/site/login";
        }
        //检查账号密码
        int expiredSeconds = rememberme ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> login = userService.login(username, password, expiredSeconds);
        if (login.containsKey("ticket")){
            Cookie cookie = new Cookie("ticket",login.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            //成功
            return "redirect:/index";
        }else{
            //失败
            model.addAttribute("usernameMsg",login.get("usernameMsg"));
            model.addAttribute("passwordMsg",login.get("passwordMsg"));
            return "/site/login";
        }
    }

    //退出
    @GetMapping("/logout")
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/login";
    }
}
