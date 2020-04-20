package com.webide.bean;

public class UserDocker {
    private Long id;
    private String dockerid;
    private String dockerfile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDockerid() {
        return dockerid;
    }

    public void setDockerid(String dockerid) {
        this.dockerid = dockerid;
    }

    public String getDockerfile() {
        return dockerfile;
    }

    public void setDockerfile(String dockerfile) {
        this.dockerfile = dockerfile;
    }
}
