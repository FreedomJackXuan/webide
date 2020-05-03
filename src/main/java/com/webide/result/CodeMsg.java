package com.webide.result;

public class CodeMsg {

    private int code;
    private String msg;

    //通用的错误码
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常：%s");
    public static CodeMsg ACCESS_LIMIT_REACHED= new CodeMsg(500104, "访问高峰期，请稍等！");
    //登录模块 5002XX
    public static CodeMsg SESSION_ERROR = new CodeMsg(500210, "Session不存在或者已经失效");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "登录密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212, "手机号不能为空");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500213, "手机号格式错误");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214, "手机号不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "密码错误");
    public static CodeMsg PRIMARY_ERROR = new CodeMsg(500216, "主键冲突");
    public static CodeMsg MOBILE_EXIST = new CodeMsg(500217, "手机号已存在");
    public static CodeMsg TOKEN_NOT_EXIST = new CodeMsg(500218, "token失效");

    //docker模块 5003XX
    public static CodeMsg DOCKER_CREATE_ERROR = new CodeMsg(500301, "docker初始化失败，请重新登陆");
    public static CodeMsg DOCKER_PIP_INSTALL = new CodeMsg(500302, "docker pip install error");

    //websocket error 5004XX
    public static CodeMsg SEND_ERROR = new CodeMsg(500401, "websocket 接受错误");

    private CodeMsg() {
    }

    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 返回带参数的错误码
     * @param args
     * @return
     */
    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }

    @Override
    public String toString() {
        return "CodeMsg [code=" + code + ", msg=" + msg + "]";
    }


}
