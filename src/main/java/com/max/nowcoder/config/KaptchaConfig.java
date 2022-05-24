package com.max.nowcoder.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @program: nowcoder
 * @description: 验证码配置类
 * @author: max-qaq
 * @create: 2022-05-21 16:17
 **/
@Configuration
public class KaptchaConfig {

    @Bean
    public Producer kaptchaProducer(){
        //验证码配置文件
        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.width","100");
        properties.setProperty("kaptcha.image.height","40");
        properties.setProperty("kaptcha.textproducer.font.size","32");
        properties.setProperty("kaptcha.textproducer.font.color","0,0,0");
        properties.setProperty("kaptcha.textproducer.char.string","0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");//验证码随机的范围
        properties.setProperty("kaptcha.textproducer.char.length","4");//验证码长度
        properties.setProperty("kaptcha.noise.impl","com.google.code.kaptcha.impl.NoNoise");//干扰


        DefaultKaptcha kaptcha = new DefaultKaptcha();
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }
}
