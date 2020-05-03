package com.webide.server;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.webide.exception.GlobalException;
import com.webide.result.CodeMsg;
import com.webide.service.DockerOptionService;
import com.webide.vo.Client;
import com.webide.vo.Message;
import com.webide.vo.PipInstall;
import com.webide.vo.Run;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

@ServerEndpoint(value = "/socketServer/{userName}")
@Component
public class WebSocketServer {
	@Autowired
	private DockerOptionService dockerOptionService;
	private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);


	private static ConcurrentHashMap<Long, Client> socketServers = new ConcurrentHashMap<>();

	/**
	 *
	 * websocket封装的session,信息推送，就是通过它来信息推送
	 */
	private Session session;

	private String userName = "";
	/**
	 *
	 * 用户连接时触发，我们将其添加到
	 * 保存客户端连接信息的socketServers中
	 *
	 * @param session
	 * @param userName
	 */
	@OnOpen
	public void open(Session session, @PathParam(value="userName")String userName){
		this.session = session;
		Long mobil = Long.parseLong(userName);
		if (socketServers.containsKey(mobil)) {
			socketServers.remove(mobil);
		}
		Client client = new Client(mobil, session);
		socketServers.put(mobil, client);
		logger.info("客户端:【{}】连接成功",userName);
	}

	/**
	 *
	 * 收到客户端发送信息时触发
	 * 我们将其推送给客户端
	 *
	 * @param message
	 */
	@OnMessage
	public void onMessage(String message){
		Long mobil = Long.parseLong(userName);
		Message jo = JSON.parseObject(message, Message.class);
		logger.info(jo.toString());
		String opetion = jo.getOption();
		String cmdmessage = "";
		switch (opetion) {
			case "1":
				PipInstall install = JSON.parseObject(jo.getData(), PipInstall.class);
				try {
					cmdmessage = dockerOptionService.do_install(mobil, install);
				} catch (IOException e) {
					throw new GlobalException(CodeMsg.DOCKER_PIP_INSTALL);
				}
				break;
			case "2":
				Run run = JSONObject.parseObject(jo.getData(), Run.class);
				try {
					cmdmessage = dockerOptionService.do_run(mobil, run);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case "3":
				break;
			case "4":
				break;
		}
		sendMessage(cmdmessage, userName);

	}

	/**
	 *
	 * 连接关闭触发，通过sessionId来移除
	 * socketServers中客户端连接信息
	 */
	@OnClose
	public void onClose(){
		Long mobil = Long.parseLong(userName);
		if (socketServers.containsKey(mobil)) {
			logger.info("客户端:【{}】close",mobil);
			socketServers.remove(mobil);
		}
	}

	/**
	 *
	 * 发生错误时触发
	 * @param error
	 */
    @OnError
    public void onError(Throwable error) {
		Long mobil = Long.parseLong(userName);
		if (socketServers.containsKey(mobil)) {
			socketServers.remove(mobil);
			logger.error("客户端:【{}】发生异常",mobil);
			error.printStackTrace();
		}
    }

	/**
	 *
	 * 信息发送的方法，通过客户端的userName
	 * 拿到其对应的session，调用信息推送的方法
	 * @param message
	 * @param userName
	 */
	public synchronized static void sendMessage(String message,String userName) {
		Client client = socketServers.get(Long.parseLong(userName));
		Session session = client.getSession();
		try {
			session.getBasicRemote().sendText(message);
		} catch (IOException e) {
			throw new GlobalException(CodeMsg.SEND_ERROR);
		}
	}

}
