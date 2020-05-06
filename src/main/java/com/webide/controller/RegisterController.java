package com.webide.controller;

import com.webide.result.Result;
import com.webide.service.UserService;
import com.webide.vo.LoginVo;
import com.webide.vo.RegisterVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegisterController {
    private static Logger log = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    UserService userService;

    @RequestMapping("/do_register")
    @ResponseBody
    public Result<String> doRegister(HttpServletResponse response, @RequestBody @Valid RegisterVo registerVo) {//加入JSR303参数校验
        log.info(registerVo.toString());
        System.out.println(registerVo.toString());
        String token = userService.register(response, registerVo);
        return Result.success(token);
    }
}
