package com.max.nowcoder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.max.nowcoder.entity.DiscussPost;

import java.util.List;


public interface DiscussPostService extends IService<DiscussPost> {
    public List<DiscussPost> findDiscussPosts(int userId , int offset , int limit);

    public int findDiscussRows(int userId);

}
