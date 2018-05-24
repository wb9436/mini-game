package com.zhiyou.wxgame.ws.websocket;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaders.Names.HOST;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.zhiyou.wxgame.configuration.WebsocketConfig;
import com.zhiyou.wxgame.ws.dispatcher.Heartbeat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

@Component
@Sharable
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object>{
	
	@Resource
	private Heartbeat heartbeat;

	private static final boolean SSL = System.getProperty("ssl") != null;
	private static final String WEBSOCKET_PATH = "/websocket";
	public static final ScheduledExecutorService heartbeatExecutor = Executors.newScheduledThreadPool(1);
	private ScheduledFuture<?> intervalScheduled;
	private ScheduledFuture<?> timeoutScheduled;

	private WebSocketServerHandshaker handshaker;

	private WebSocket webSocket;

	@Resource
	private WebSocketHandler webSocketHandler;
	
	private static AtomicLong SIDS = new AtomicLong(1);

	protected final Logger logger = Logger.getLogger(getClass());

	@Override
	public void channelRead0(ChannelHandlerContext ctx, Object msg) {
		if (msg instanceof FullHttpRequest) {
			handleHttpRequest(ctx, (FullHttpRequest) msg);
		} else if (msg instanceof WebSocketFrame) {
			handleWebSocketFrame(ctx, (WebSocketFrame) msg);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
		// Handle a bad request.
		if (!req.getDecoderResult().isSuccess()) {
			sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
			return;
		}

		// Allow only GET methods.
		if (req.getMethod() != GET) {
			sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN));
			return;
		}

		// Send the demo page and favicon.ico
		if ("/".equals(req.getUri())) {
			boolean debug = false;
			if (debug) {
				ByteBuf content = WebSocketServerIndexPage.getContent(getWebSocketLocation(req));
				FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, OK, content);
				res.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
				HttpHeaders.setContentLength(res, content.readableBytes());
				sendHttpResponse(ctx, req, res);
			}
			return;
		}
		if ("/favicon.ico".equals(req.getUri())) {
			FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND);
			sendHttpResponse(ctx, req, res);
			return;
		}

		// Handshake
		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(getWebSocketLocation(req),
				null, true);
		handshaker = wsFactory.newHandshaker(req);
		if (handshaker == null) {
			WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
		} else {
			handshaker.handshake(ctx.channel(), req);
		}
//		webSocketHandler = new WebSocketHandler();
		webSocket = new WebSocket(ctx, req, SIDS.getAndIncrement());
		webSocketHandler.onConnect(webSocket);
		restartHeartbeat();
	}

	private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {

		// Check for closing frame
		if (frame instanceof CloseWebSocketFrame) {
			handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
			webSocketHandler.onColse(webSocket);
			return;
		}
		if (frame instanceof PingWebSocketFrame) {
			ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		if (!(frame instanceof TextWebSocketFrame)) {
			throw new UnsupportedOperationException(
					String.format("%s frame types not supported", frame.getClass().getName()));
		}

		String message = ((TextWebSocketFrame) frame).text();
		webSocket.setUpdateTime(System.currentTimeMillis());
		DataPacket request = JSONObject.parseObject(message, DataPacket.class);
		webSocketHandler.onMessage(webSocket, request);
		restartHeartbeat();
	}

	private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
		// Generate an error page if response getStatus code is not OK (200).
		if (res.getStatus().code() != 200) {
			ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
			res.content().writeBytes(buf);
			buf.release();
			HttpHeaders.setContentLength(res, res.content().readableBytes());
		}

		// Send the response and close the connection if necessary.
		ChannelFuture f = ctx.channel().writeAndFlush(res);
		if (!HttpHeaders.isKeepAlive(req) || res.getStatus().code() != 200) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		webSocketHandler.onError(webSocket, cause);
	}

	private static String getWebSocketLocation(FullHttpRequest req) {
		String location = req.headers().get(HOST) + WEBSOCKET_PATH;
		if (SSL) {
			return "wss://" + location;
		} else {
			return "ws://" + location;
		}
	}

	/**
	 * 停止断线检测
	 */
	private void stopTimeoutScheduled() {
		if (timeoutScheduled != null) {
			if (!timeoutScheduled.isCancelled() && !timeoutScheduled.isDone()) {
				timeoutScheduled.cancel(true);
			}
		}
	}

	/**
	 * 停止送心跳检测
	 */
	private void stopIntervalScheduled() {
		if (intervalScheduled != null) {//
			if (!intervalScheduled.isCancelled() && !intervalScheduled.isDone()) {
				intervalScheduled.cancel(true);
			}
		}
	}

	/**
	 * 断线后并取消定时发送心跳信息
	 */
	private void resetTimeoutScheduled() {
		timeoutScheduled = heartbeatExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				webSocket.close();
				stopIntervalScheduled();
			}
		}, WebsocketConfig.getTimeout(), TimeUnit.SECONDS);
	}

	/**
	 * 重新发送心跳包
	 */
	private void resentHeartbeatPacket() {
		intervalScheduled = heartbeatExecutor.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (webSocket != null && webSocket.isOpen()) {
					// 发送心跳包
					boolean success = heartbeat.send(webSocket);
					if (success) {
						stopTimeoutScheduled();
						resetTimeoutScheduled();
					}
				}
			}
		}, WebsocketConfig.getIntervalTime(), WebsocketConfig.getIntervalTime(), TimeUnit.SECONDS);
	}

	/**
	 * 启动或重新启动心跳检测
	 */
	private void restartHeartbeat() {

		stopIntervalScheduled();

		stopTimeoutScheduled();

		resetTimeoutScheduled();

		resentHeartbeatPacket();

	}

}
