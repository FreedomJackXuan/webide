package com.webide.controller;

import com.webide.result.Result;
import com.webide.service.UserService;
import com.webide.vo.LoginVo;
import com.webide.vo.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<UserInfo> doLogin(HttpServletResponse response, @RequestBody @Valid LoginVo loginVo) {
        log.info(loginVo.toString());
        UserInfo token = userService.login(response, loginVo);
        return Result.success(token);
    }

    @RequestMapping("/token")
    @ResponseBody
    public Result<UserInfo> tokenLogin(NativeWebRequest nativeWebRequest) {
        UserInfo token = userService.tokenLogin(nativeWebRequest);
        return Result.success(token);
    }
}
