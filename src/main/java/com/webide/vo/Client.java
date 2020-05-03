package com.webide.vo;

import javax.websocket.Session;
import java.io.Serializable;

public class Client implements Serializable {

    private static final long serialVersionUID = 8957107006902627635L;

    private Long userName;

    private Session session;

    public Long getUserName() {
        return userName;
    }

    public void setUserName(Long userName) {
        this.userName = userName;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Client(Long userName, Session session) {
        this.userName = userName;
        this.session = session;
    }

    public Client() {
    }

    @Override
    public String toString() {
        return "Client{" +
                "userName='" + userName + '\'' +
                ", session='" + session + '\'' +
                '}';
    }

}
