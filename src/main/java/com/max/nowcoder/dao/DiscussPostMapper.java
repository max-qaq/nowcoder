package com.max.nowcoder.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.max.nowcoder.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper extends BaseMapper<DiscussPost> {
    /**
     * 分页查询帖子
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    List<DiscussPost> selectDiscussPosts(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 查询一共有多少页
     * @return
     */
    int selectDiscussPostRows(@Param("userId") int userId);



}
