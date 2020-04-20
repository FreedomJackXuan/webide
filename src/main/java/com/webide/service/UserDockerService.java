package com.webide.service;

import com.webide.mapper.UserMapper;
import com.webide.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDockerService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisService redisService;
}
