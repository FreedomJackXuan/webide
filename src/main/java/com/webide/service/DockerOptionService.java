package com.webide.service;

import com.webide.bean.UserDocker;
import com.webide.mapper.UserDockerMapper;
import com.webide.redis.RedisService;
import com.webide.util.OSSDownload;
import com.webide.util.StringUtil;
import com.webide.vo.Message;
import com.webide.vo.PipInstall;
import com.webide.vo.Run;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class DockerOptionService {
    @Autowired
    UserDockerService dockerService;

    @Autowired
    RedisService redisService;

    private static Logger log = LoggerFactory.getLogger(UserDockerService.class);

    public String do_install(Long mobil, PipInstall install) throws IOException {
        UserDocker docker = dockerService.getById(mobil);

        String dockerid = docker.getDockerid();
        String comd = "sh shell/install.sh " + dockerid +" " + install.getOrder();
        Runtime runtime = Runtime.getRuntime();
        Process pro = runtime.exec(comd);
        BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
        StringBuffer strbr = new StringBuffer();
        String line;

        while ((line = br.readLine())!= null) {
            strbr.append(line);
        }
        log.info(strbr.toString());

        return strbr.toString();
    }

    public String do_run(Long mobil, Run run) throws IOException {
        UserDocker docker = dockerService.getById(mobil);
//        String date = run.getDate();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date updateDate = null;
//        try {
//            updateDate = sdf.parse(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        String[] filepath = run.getPath().split(";");

        OSSDownload.download(filepath, docker.getDockerfile());

        String dockerid = docker.getDockerid();
        String comd = "sh shell/run.sh " + dockerid +" /" + docker.getDockerid()+"/"+run.getPath();

        Runtime runtime = Runtime.getRuntime();
        Process pro = runtime.exec(comd);
        BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
        StringBuffer strbr = new StringBuffer();
        String line;

        while ((line = br.readLine())!= null) {
            strbr.append(line);
        }
        log.info(strbr.toString());

        return strbr.toString();
    }
}
