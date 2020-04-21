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

    public UserDocker createDocker(User user) {
        long id = user.getId();

        File file = new File("/home/jac/"+user.getId());
        if (!file.exists()){
            file.mkdir();
        }
        UserDocker udocker = getById(id);
        boolean rundocker = false;
        if (udocker.getDockerid() == null || ("").equals(udocker.getDockerid())){
            rundocker = true;
            Process process=Runtime.getRuntime().exec(new String[]
                    {"/home/jingbao/桌面/shell/startDocker.sh","7777",
                            "/home/jingbao/桌面/"+data.getMac(),
                            "/home"},null,null);
            BufferedReader read=new BufferedReader(new InputStreamReader(process
                    .getInputStream()));
            process.waitFor();
            String res="";
            String line="";
            while ((line=read.readLine())!=null){
                res=res+line;
            }
            System.out.println();
            if (res==""||res.equals("")){
            }else {
                String[] id=res.split("[  ]");
                operating.set(data.getMac()+"_docker",id[8]);
                data.setDockerId(id[8]);
            }
        } else {

        }
        return udocker;
    }

    public String getFilePaht() {
        return "";
    }
}
