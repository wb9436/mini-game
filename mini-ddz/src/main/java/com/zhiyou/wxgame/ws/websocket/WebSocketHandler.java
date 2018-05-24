package com.zhiyou.wxgame.ws.websocket;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.zhiyou.wxgame.configuration.ApplicationBeanManager;
import com.zhiyou.wxgame.ws.dispatcher.Dispatcher;
import com.zhiyou.wxgame.ws.dispatcher.Handler;

@Component
public class WebSocketHandler{
	
    protected final Logger logger = Logger.getLogger(getClass());
   
    /**
     * 连接成功
     * 
     * @param webSocket
     */
    public void onConnect(final WebSocket webSocket) {
        logger.info("onConnect:sessionId=" + webSocket.getSessionId());
    }

    /**
     * 关闭事件
     * 
     * @param webSocket
     */
    public void onColse(WebSocket webSocket) {
        logger.info("onColse:sessionId=" + webSocket.getSessionId());
        WebSocketManager.remove(webSocket);
        webSocket.close();
    }

    /**
     * 接收数据
     * 
     * @param webSocket
     * @param message
     */
    public void onMessage(WebSocket webSocket, DataPacket request) {
		String handlerClass = Dispatcher.getHandlerClass(request.getCmd());
		if (!StringUtils.isEmpty(handlerClass)) {
			Class<?> clzz = null;
			try {
				clzz = Class.forName(handlerClass);
				Handler handler = (Handler) ApplicationBeanManager.getBean(clzz);
				if (handler != null) {
					handler.handle(webSocket, request);
				} else {
					logger.error("Handler error." + handlerClass);
					webSocket.close();
				}
			} catch (Exception e) {
				logger.error("onMessage error.", e);
				webSocket.close();
			}
		} else {
			logger.error("Cmd error." + request.getCmd());
		}
    }

    /**
     * 异常
     * 
     * @param webSocket
     * @param cause
     */
    public void onError(WebSocket webSocket, Throwable cause) {
        logger.info("onError:sessionId=" + webSocket.getSessionId() + ";cause=" + cause.getMessage());
        webSocket.close();
        WebSocketManager.remove(webSocket);
    }
}
