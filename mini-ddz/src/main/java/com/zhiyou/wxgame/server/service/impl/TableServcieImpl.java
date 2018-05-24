package com.zhiyou.wxgame.server.service.impl;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zhiyou.wxgame.server.service.ITableService;
import com.zhiyou.wxgame.util.service.BService;
import com.zhiyou.wxgame.ws.websocket.DataPacket;
import com.zhiyou.wxgame.ws.websocket.WebSocket;

@Service
public class TableServcieImpl extends BService implements ITableService{
	
	@Override
	public void doGetMessage(WebSocket webSocket, JSONObject data) {
		JSONObject resultData = new JSONObject();
		resultData.put("message", "我收到数据了");
		DataPacket response = new DataPacket(2000, resultData);
		webSocket.send(response);

		logger.info("你好啊  测试Handler : data=" + data.toJSONString());
	}

	@Override
	public void doOffline(WebSocket webSocket) {
		logger.info("我又掉线了...userId = " + webSocket.getUserid());
	}
	
	
}
