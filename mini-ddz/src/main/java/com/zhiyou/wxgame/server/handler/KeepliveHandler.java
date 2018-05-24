package com.zhiyou.wxgame.server.handler;

import org.springframework.stereotype.Component;

import com.zhiyou.wxgame.ws.dispatcher.BaseHandler;
import com.zhiyou.wxgame.ws.dispatcher.Handler;
import com.zhiyou.wxgame.ws.websocket.DataPacket;
import com.zhiyou.wxgame.ws.websocket.WebSocket;

@Component
public class KeepliveHandler extends BaseHandler implements Handler{

    @Override
    public void handle(WebSocket webSocket, DataPacket request) {
    	System.err.println("心跳了...");
    }

}


