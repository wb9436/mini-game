package com.zhiyou.wxgame.server.service;

import com.alibaba.fastjson.JSONObject;
import com.zhiyou.wxgame.ws.websocket.WebSocket;

public interface ITableService {

	public void doGetMessage(WebSocket webSocket, JSONObject data);

	public void doOffline(WebSocket webSocket);

}
