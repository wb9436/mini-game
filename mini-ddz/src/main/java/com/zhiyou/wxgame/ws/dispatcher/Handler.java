package com.zhiyou.wxgame.ws.dispatcher;

import com.zhiyou.wxgame.ws.websocket.DataPacket;
import com.zhiyou.wxgame.ws.websocket.WebSocket;

/**
 * 逻辑处理，每次交互的请求都要实现本接口.
 * @author WB
 * 
 */
public interface Handler{

	public void handle(WebSocket webSocket, DataPacket request);

}
