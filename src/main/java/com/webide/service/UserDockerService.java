package com.webide.service;

import com.webide.bean.User;
import com.webide.bean.UserDocker;
import com.webide.mapper.UserDockerMapper;
import com.webide.mapper.UserMapper;
import com.webide.redis.RedisService;
import com.webide.redis.UserKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class UserDockerService {
    @Autowired
    UserDockerMapper userMapper;

    @Autowired
    RedisService redisService;

    public UserDocker getById(long id) {
        //对象缓存
        UserDocker docker = redisService.get(UserKey.getById, "" + id, UserDocker.class);
        if (docker != null) {
            return docker;
        }
        //取数据库
        docker = userMapper.getById(id);
        //再存入缓存
        if (docker != null) {
            redisService.set(UserKey.getById, "" + id, docker);
        }
        return docker;
    }

    public UserDocker createDocker(User user) throws Exception {
        long id = user.getId();
        String path = "/home/jac/"+user.getId();
        File file = new File(path);
        if (!file.exists()){
            file.mkdir();
        }
        UserDocker udocker = getById(id);
        boolean rundocker = false;
        if (udocker.getDockerid() == null || ("").equals(udocker.getDockerid())){
            rundocker = true;
        } else {
            int docker_status = isRunDocker(udocker.getDockerid(), path);
        }
        if (rundocker) {

        }
        return udocker;
    }

    public String getFilePaht() {
        return "";
    }

    public Integer isRunDocker(String dockerid, String path) throws Exception {
        int readline = 0;
        String comd = "sh shell/dockerps.sh "+dockerid;
        Process process = Runtime.getRuntime().exec(comd);
        BufferedReader read = new BufferedReader(new InputStreamReader(process.getInputStream()));
        process.waitFor();
        StringBuffer res = new StringBuffer();
        String line = "";

        int result = -1;
        while ((line = read.readLine()) != null) {
            readline += 1;
            if (line.contains("CONTAINER")){
                continue;
            }
            if (line.contains("UP")) {
                result = 1;
            } else {
                result = 2;
            }
        }
        if (readline == 1) {
            result = 0;
        }

        return result;
    }
}
