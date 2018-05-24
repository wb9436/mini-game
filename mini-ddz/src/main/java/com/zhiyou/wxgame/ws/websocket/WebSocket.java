package com.zhiyou.wxgame.ws.websocket;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * webSocket对象
 * 
 * @author guiyuan
 */
public class WebSocket {

    protected final Logger logger = Logger.getLogger(getClass());
    private ChannelHandlerContext ctx;
    private HttpRequest request;
    private long sessionId;
    private long createTime;
    private long updateTime;
    private int userid;
    private String remoteAddr;
    private String tableId;
    private long seq = 0L;

    public WebSocket(ChannelHandlerContext ctx, HttpRequest request, long sessionId) {
        this.setRequest(request);
        this.setCtx(ctx);
        this.setSessionId(sessionId);
        long l = System.currentTimeMillis();
        this.setCreateTime(l);
        this.setUpdateTime(l);
        //this.remoteAddr = ctx.channel().remoteAddress().a
        InetSocketAddress addr = (InetSocketAddress)ctx.channel().remoteAddress();
        this.remoteAddr = addr.getAddress().getHostAddress();
    }

    public long incr() {
        long l = this.seq + 1;
        this.seq = l;
        return this.seq;
    }
    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public ChannelHandlerContext getCtx() {
        return ctx;
    }

    private void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public HttpRequest getRequest() {
        return request;
    }

    private void setRequest(HttpRequest request) {
        this.request = request;
    }

    public long getSessionId() {
        return sessionId;
    }

    private void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    protected ChannelFuture send0(String message) {
        ChannelFuture future = this.ctx.channel().writeAndFlush(new TextWebSocketFrame(message));
        if (logger.isDebugEnabled()) {
            logger.debug("delay send,userid=" + userid + ";" + message);
        }
        return future;
    }

    public void flush() {
        this.ctx.channel().flush();
    }

    protected ChannelFuture sendAndFlush(String message) {
        ChannelFuture future = this.ctx.channel().writeAndFlush(new TextWebSocketFrame(message));
        if (logger.isDebugEnabled()) {
            logger.debug("delay send ,userid=" + userid + " status is:" + future.isSuccess() + ";" + message);
        }
        return future;
    }

    public boolean send(final DataPacket response) {
        response.setSeq(incr());
        ChannelFuture future = sendAndFlush(response);
        future.addListener(new ChannelFutureListener() {
            
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
            }
        });
        return true;
    }
    
    public boolean sendHeart(final DataPacket response) {
        ChannelFuture future = sendAndFlush(response);
        future.addListener(new ChannelFutureListener() {
            
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
            }
        });
        return true;
    }

    protected ChannelFuture sendAndFlush(DataPacket response) {
        
        ChannelFuture future = this.ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(response)));
//        if (logger.isDebugEnabled()) {
//            logger.debug("immediate send:"+response.toString());
//        }
        if (logger.isDebugEnabled() && response.getCmd() != 10000) {
            logger.debug("send msg: cmd="+response.getCmd()+", RecvUserId:userId="+this.userid+",sessionId="+this.getSessionId());
        }
        return future;
    }

    public ChannelFuture close() {
        WebSocket websoket = WebSocketManager.getWebSocket(userid);
        if (websoket != null && websoket.getSessionId() == sessionId) {
            WebSocketManager.remove(this);// 根据SessionId来清除WebSocket
        }
        ChannelFuture future = this.ctx.channel().close();
        future.addListener(ChannelFutureListener.CLOSE);
        if (logger.isDebugEnabled()) {
            logger.debug("close websocket, userid=" + userid + " sessionId:" + this.sessionId);
        }
        return future;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isOpen() {
        return this.ctx.channel().isOpen();
    }

    public boolean isActive() {
        return this.ctx.channel().isActive();
    }

    /**
     * 获得在线时间
     * 
     * @return
     */
    public int getOnlineTime() {
        return (int) ((updateTime - createTime) / 1000);
    }

    @Override
    public String toString() {
        return "WebSocket [sessionId=" + sessionId + ", userid=" + userid + ", createTime=" + createTime
                + ", updateTime=" + updateTime + "]";
    }
    
    /**
     * 玩家离开桌子清除桌子信息
     */
    public void cleanTable(){
        this.tableId = "";
    }
}
