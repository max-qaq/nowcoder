package com.max.nowcoder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @program: nowcoder
 * @description:
 * @author: max-qaq
 * @create: 2022-05-20 11:19
 **/
@Controller
public class LoginController {
    @GetMapping("/register")
    public String getRegisterPage(){
        return "/site/register";
    }
}
