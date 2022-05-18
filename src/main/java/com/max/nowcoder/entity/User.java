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
    private int id;
    private String username;
    private String password;
    private String salt;
    private String email;
    private int type;
    private int status;
    private String activationCode;
    private String headerUrl;
    private Date createTime;
}
