package com.zhiyou.wxgame.ws.dispatcher;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.zhiyou.wxgame.server.service.ITableService;
import com.zhiyou.wxgame.ws.websocket.WebSocket;

@Component
public class Offline {

	@Resource
	private ITableService tableService;
	
	public void process(WebSocket webSocket) {
		tableService.doOffline(webSocket);
	}

}
