package com.webide.service;

import com.alibaba.druid.util.StringUtils;
import com.webide.bean.User;
import com.webide.exception.GlobalException;
import com.webide.mapper.UserMapper;
import com.webide.redis.RedisService;
import com.webide.redis.UserKey;
import com.webide.result.CodeMsg;
import com.webide.util.MD5Util;
import com.webide.util.UUIDUtil;
import com.webide.vo.LoginVo;
import com.webide.vo.RegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by jiangyunxiong on 2018/5/22.
 */
@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisService redisService;

    @Autowired
    UserDockerService userDockerService;


    public static final String COOKIE_NAME_TOKEN = "token";

    public User getById(long id) {
        //对象缓存
        User user = redisService.get(UserKey.getById, "" + id, User.class);
        if (user != null) {
            return user;
        }
        //取数据库
        user = userMapper.getById(id);
        //再存入缓存
        if (user != null) {
            redisService.set(UserKey.getById, "" + id, user);
        }
        return user;
    }

    /**
     * 典型缓存同步场景：更新密码
     */
    public boolean updatePassword(String token, long id, String formPass) {
        //取user
        User user = getById(id);
        if(user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //更新数据库
        User toBeUpdate = new User();
        toBeUpdate.setId(id);
//        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
        userMapper.update(toBeUpdate);
        //更新缓存：先删除再插入
        redisService.delete(UserKey.getById, ""+id);
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(UserKey.token, token, user);
        return true;
    }

    public String login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        System.out.println("formPass "+ formPass);
        //判断手机号是否存在
        User user = getById(Long.parseLong(mobile));
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.inputPassToDbPass(formPass, saltDB);
        System.out.println("dbPass "+dbPass+" saltDB "+ saltDB + " calcPass "+ calcPass);
        if (!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //生成唯一id作为token

        try {
            userDockerService.createDocker(user);
        } catch (Exception e) {
            throw new GlobalException(CodeMsg.DOCKER_CREATE_ERROR);
        }

        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return token;
    }

    /**
     * 将token做为key，用户信息做为value 存入redis模拟session
     * 同时将token存入cookie，保存登录状态
     */
    public void addCookie(HttpServletResponse response, String token, User user) {
        redisService.set(UserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(UserKey.token.expireSeconds());
        cookie.setPath("/");//设置为网站根目录
        response.addCookie(cookie);
    }

    /**
     * 根据token获取用户信息
     */
    public User getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        User user = redisService.get(UserKey.token, token, User.class);
        //延长有效期，有效期等于最后一次操作+有效期
        if (user != null) {
            addCookie(response, token, user);
        }
        return user;
    }


    public String register(HttpServletResponse response, RegisterVo registerVo) {
        if (registerVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = registerVo.getMobile();
        String nickname = registerVo.getNickname();
        String formPass = registerVo.getPassword();
        //判断手机号是否存在
        User user = getById(Long.parseLong(mobile));
        if (user != null) {
            throw new GlobalException(CodeMsg.MOBILE_EXIST);
        }
        user = new User();
        user.setId(Long.parseLong(mobile));
        user.setNickname(nickname);
        user.setSalt(MD5Util.getSalt());
        user.setPassword(MD5Util.inputPassToDbPass(formPass, user.getSalt()));
        user.setRegisterDate(new Date());
        System.out.println(user.toString());
        userMapper.insertUser(user);
        //生成唯一id作为token

        return "true";
    }

    public String tokenLogin(NativeWebRequest nativeWebRequest){
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);

        String paramToken = request.getParameter(UserService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request);
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return null;
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        User user = getByToken(response, token);
        if (user == null) {
            throw new GlobalException(CodeMsg.TOKEN_NOT_EXIST);
        }

        try {
            userDockerService.createDocker(user);
        } catch (Exception e) {
            throw new GlobalException(CodeMsg.DOCKER_CREATE_ERROR);
        }

        return token;
    }

    //遍历所有cookie，找到需要的那个cookie
    private String getCookieValue(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(COOKIE_NAME_TOKEN)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
