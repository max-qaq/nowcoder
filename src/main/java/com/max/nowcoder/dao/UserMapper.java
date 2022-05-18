package com.max.nowcoder.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.max.nowcoder.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
