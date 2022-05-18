package com.max.nowcoder;

import com.max.nowcoder.dao.DiscussPostMapper;
import com.max.nowcoder.entity.DiscussPost;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class NowcoderApplicationTests {

    @Autowired
    DiscussPostMapper discussPostMapper;

    @Test
    void contextLoads() {
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(0, 0, 5);
        for (DiscussPost discussPost : discussPosts){
            System.out.println(discussPost);
        }
        System.out.println(discussPostMapper.selectDiscussPostRows(0));
    }

}
