package com.webide.vo;

public class Message {
    private String option; // 1 INSTALL, 2 RUN, 3 DEBUG
    private String data;

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
