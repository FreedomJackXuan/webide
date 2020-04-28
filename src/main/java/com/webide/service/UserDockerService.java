package com.webide.service;

import com.webide.bean.User;
import com.webide.bean.UserDocker;
import com.webide.mapper.UserDockerMapper;
import com.webide.mapper.UserMapper;
import com.webide.redis.RedisService;
import com.webide.redis.UserKey;
import com.webide.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class UserDockerService {
    @Autowired
    UserDockerMapper userMapper;

    @Autowired
    RedisService redisService;

    private ReentrantLock lock = new ReentrantLock();
    private static Logger log = LoggerFactory.getLogger(UserDockerService.class);

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
        boolean restardocker = false;
        if (udocker.getDockerid() == null || ("").equals(udocker.getDockerid())){
            rundocker = true;
        } else {
            int docker_status = isRunDocker(udocker.getDockerid(), path);
            if (docker_status == 0) {
                rundocker = true;
            } else if (docker_status == 2) {
                restardocker = true;
            }
        }
        if (rundocker) {
            String comd = "sh shell/startDocker.sh " + path +" " +"/"+user.getId()+" "+user.getId();
            Runtime runtime = Runtime.getRuntime();
            Process pro = runtime.exec(comd);
            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            StringBuffer strbr = new StringBuffer();
            String line;

            while ((line = br.readLine())!= null) {
                strbr.append(line);
            }
            log.info(strbr.toString());
            String dockerid = StringUtil.subStrByStrAndLen(strbr.toString(), 12);

            if (udocker == null) {
                udocker = new UserDocker();
                udocker.setId(id);
                udocker.setDockerid(dockerid);
                udocker.setDockerfile(path);
                userMapper.insertUser(udocker);
            } else {
                udocker.setDockerid(dockerid);
                udocker.setDockerfile(path);
                userMapper.update(udocker);
            }

        }

        if (restardocker) {
            String comd = "sh shell/docker_restart.sh " + udocker.getDockerid();
            Runtime runtime = Runtime.getRuntime();
            Process pro = runtime.exec(comd);
            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            StringBuffer strbr = new StringBuffer();
            String line;

            while ((line = br.readLine())!= null) {
                strbr.append(line);
            }
            log.info(line);
        }

        return udocker;
    }

    public String getFilePaht() {
        return "";
    }

    public Integer isRunDocker(String dockerid, String path) throws Exception {
        lock.lock();
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

        lock.unlock();
        return result;
    }
}
