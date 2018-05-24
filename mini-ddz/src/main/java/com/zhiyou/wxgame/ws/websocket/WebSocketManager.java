package com.zhiyou.wxgame.ws.websocket;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.zhiyou.wxgame.ws.dispatcher.Offline;

@Component
public class WebSocketManager implements ApplicationContextAware {

	private static Offline offline;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		offline = applicationContext.getBean(Offline.class);
	}

    /** 保存userid,websocket */
    private static final ConcurrentHashMap<String, WebSocket> websockets = new ConcurrentHashMap<String, WebSocket>();

    private static String getWsSocketKey(int userId) {
        return String.valueOf(userId);
    }
    /**
     * 绑定userid与webSocket连接
     * 
     * @param userid
     * @param webSocket
     */
    public static void bindWebSocket(int userId, WebSocket webSocket) {
        websockets.put(getWsSocketKey(userId), webSocket);
        webSocket.setUserid(userId);
    }

    public static void remove(WebSocket webSocket) {
        if (webSocket == null) {
            return;
        }
        int userId = webSocket.getUserid();
        if (userId > 0) {
            websockets.remove(getWsSocketKey(userId));
            offline.process(webSocket);            
        }
    }

    /**
     * 获取对应用户的websocket连接
     * 
     * @param userid
     * @return
     */
    public static WebSocket getWebSocket(int userid) {
        return websockets.get(getWsSocketKey(userid));
    }

    public static boolean isOnline(int userId) {
        return websockets.containsKey(getWsSocketKey(userId));
    }

    public static void broadcast(int removeRoleId, DataPacket resp) {
        Collection<WebSocket> colls = websockets.values();

        for (Iterator<WebSocket> it = colls.iterator(); it.hasNext();) {
            WebSocket webSocket = it.next();
            if (webSocket.getUserid() == removeRoleId) {
                continue;
            }
            webSocket.sendAndFlush(resp);
        }
    }
    
    /**
     * 全服广播
     * 
     */
    public static void broadcast(DataPacket resp) {
        Collection<WebSocket> colls = websockets.values();

        for (Iterator<WebSocket> it = colls.iterator(); it.hasNext();) {
            WebSocket webSocket = it.next();
            webSocket.sendAndFlush(resp);
        }
    }

    /**
     * 全服广播
     * 
     * @param selfWebSocket
     * @param message
     */
    public static void broadcast(WebSocket selfWebSocket, DataPacket resp) {
        Collection<WebSocket> colls = websockets.values();

        for (Iterator<WebSocket> it = colls.iterator(); it.hasNext();) {
            WebSocket webSocket = it.next();
            if (selfWebSocket != null && selfWebSocket.getSessionId() == webSocket.getSessionId()) {
                // 不需要把自己发送的消息回写客户端
                continue;
            }
            webSocket.send(resp);
        }
    }
    
    /**
     * 把消息回写客户端
     * 
     * @param userid
     * @param resp
     */
    public static void send(int userid, DataPacket resp) {
        WebSocket webSocket = websockets.get(getWsSocketKey(userid));
        if (webSocket != null) {
            webSocket.send(resp);
        } 
    }


    /**
     * 关闭websocket
     * 
     * @param userid
     */
    public static void close(int userid) {
        websockets.get(getWsSocketKey(userid)).close();
    }


    /**
     * 获取在线列表
     * 
     * @return
     */
    public static Collection<WebSocket> getWebSockets() {
        return websockets.values();
    }
    
    public static int getOnlineCount() {
        return websockets.size();
    }

}
