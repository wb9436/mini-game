package com.zhiyou.wxgame.ws.dispatcher;

import org.springframework.stereotype.Component;

import com.zhiyou.wxgame.ws.websocket.DataPacket;
import com.zhiyou.wxgame.ws.websocket.WebSocket;

@Component
public class Heartbeat {

	public boolean send(WebSocket webSocket) {
		return webSocket.sendHeart(new DataPacket(10000));
	}

}
