package com.max.nowcoder.entity;

import lombok.Data;

import java.util.Date;

/**
 * @program: nowcoder
 * @description:帖子实体类
 * @author: max-qaq
 * @create: 2022-05-18 17:57
 **/
@Data
public class DiscussPost {
    private Integer id;

    private Integer userId;

    private String title;

    private String content;

    private Integer type;
    //帖子类型：0普通1置顶
    private Integer status;
    //帖子状态，0精华1置顶2拉黑
    private Date createTime;

    private Integer commentCount;

    private Double score;

}
