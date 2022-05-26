package com.max.nowcoder.controller;

import com.max.nowcoder.annotation.LoginRequired;
import com.max.nowcoder.entity.User;
import com.max.nowcoder.service.UserService;
import com.max.nowcoder.utils.CommunityUtil;
import com.max.nowcoder.utils.HostHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @program: nowcoder
 * @description:账户修改
 * @author: max-qaq
 * @create: 2022-05-25 20:25
 **/
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @Value("${community.path.domain}")
    private String domain;
    @Value("${community.path.upload}")
    private String uploadPath;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    //用户设置页面
    @LoginRequired
    @GetMapping("/setting")
    public String getSetting(){
        return "/site/setting";
    }

    //上传头像
    @PostMapping("/upload")
    @LoginRequired
    public String uploadHeader(MultipartFile headerImg, Model model) throws IOException {
        //文件不存在的情况
        if (headerImg == null){
            model.addAttribute("error","您还没有选择图片");
            return "/site/setting";
        }
        //文件存在
        //获取文件后缀
        String filename = headerImg.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        log.info("filename"+filename);
        log.info("suffix"+suffix);
        if (StringUtils.isBlank(suffix)){
            model.addAttribute("error","文件格式不正确");
            return "/site/setting";
        }
        //生成随机文件名
        filename = CommunityUtil.generateUUID() + suffix;
        //拼接文件路径
        File file = new File(uploadPath + "/" + filename);
        //转换到目标文件
        headerImg.transferTo(file);
        //更新头像路径(web访问路径)
        User user = hostHolder.getUser();
        String headUrl = domain + contextPath + "/user/header/" + filename;
        log.info("headurl"+headUrl);
        userService.updateHeader(user.getId(),headUrl);

        return "redirect:/index";
    }

    //获取头像
    @GetMapping("/header/{filename}")
    public void getHeader(@PathVariable("filename") String filename, HttpServletResponse response){
        //服务器存放路径
        filename = uploadPath + "/" + filename;
        //文件后缀
        String suffix = filename.substring(filename.lastIndexOf("."));
        //响应图片
        try(
                FileInputStream fis = new FileInputStream(filename);
                ){
            response.setContentType("image/"+suffix);
            OutputStream outputStream = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1){
                outputStream.write(buffer,0,b);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    //更改密码
    @PostMapping("/updatePassword")
    public String updatePassword(String oldPassword, String newPassword1, String newPassword2 ,Model model){
        if (!newPassword1.equals(newPassword2)){
            model.addAttribute("newPasswordMsg","两次密码不相同");
            return "/site/setting";
        }
        Map<String , Object> map = userService.updatePassword(oldPassword,newPassword1,model);
        if (map.containsKey("oldPasswordMsg")){
            //旧密码错误
            model.addAttribute("oldPasswordMsg",map.get("oldPasswordMsg"));
            return "/site/setting";
        }
        //密码正确
        return "redirect:/logout";
    }
}
