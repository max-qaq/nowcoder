package com.max.nowcoder.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.max.nowcoder.entity.LoginTicket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface LoginTicketMapper extends BaseMapper<LoginTicket> {
    @Update({
            "update login_ticket set status = #{status} where ticket = #{ticket}"
    })
    int updateStatus(@Param("ticket") String ticket, @Param("status") int status);
}
