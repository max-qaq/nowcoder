package com.max.nowcoder.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.max.nowcoder.dao.DiscussPostMapper;
import com.max.nowcoder.entity.DiscussPost;
import com.max.nowcoder.service.DiscussPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: nowcoder
 * @description:
 * @author: max-qaq
 * @create: 2022-05-18 19:04
 **/
@Service
public class DiscussPostServiceImpl extends ServiceImpl<DiscussPostMapper, DiscussPost> implements DiscussPostService {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Override
    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit) {
        return discussPostMapper.selectDiscussPosts(userId,offset,limit);
    }

    @Override
    public int findDiscussRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }
}
