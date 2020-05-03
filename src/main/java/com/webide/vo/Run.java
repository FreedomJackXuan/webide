package com.webide.vo;

public class Run {
    private String path; //修改过的文件 “XXX;XXX;XXX”
    private String main; //主函数文件地址

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }
}
