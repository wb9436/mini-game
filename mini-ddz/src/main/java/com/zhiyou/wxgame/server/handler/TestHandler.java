package com.zhiyou.wxgame.server.handler;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.zhiyou.wxgame.server.service.ITableService;
import com.zhiyou.wxgame.ws.dispatcher.BaseHandler;
import com.zhiyou.wxgame.ws.dispatcher.Handler;
import com.zhiyou.wxgame.ws.websocket.DataPacket;
import com.zhiyou.wxgame.ws.websocket.WebSocket;
import com.zhiyou.wxgame.ws.websocket.WebSocketManager;

@Component
public class TestHandler extends BaseHandler implements Handler{

	@Resource
	private ITableService tableService;

	@Override
	public void handle(WebSocket webSocket, DataPacket request) {
		WebSocketManager.bindWebSocket(100123, webSocket);
		tableService.doGetMessage(webSocket, getData(webSocket, request));
	}

}
