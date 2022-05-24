package com.max.nowcoder.entity;

import lombok.Data;

import java.util.Date;

/**
 * @program: nowcoder
 * @description:
 * @author: max-qaq
 * @create: 2022-05-18 19:25
 **/
@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String salt;
    private String email;
    private Integer type;
    private Integer status;
    private String activationCode;
    private String headerUrl;
    private Date createTime;
}
