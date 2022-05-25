package com.max.nowcoder.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.util.Date;

/**
 * @program: nowcoder
 * @description:
 * @author: max-qaq
 * @create: 2022-05-24 19:17
 **/
@Data
public class LoginTicket {
    private Integer id;
    private Integer userId;

    private String ticket;

    @TableLogic(value = "0",delval = "1")
    private Integer status;

    private Date expired;
}
