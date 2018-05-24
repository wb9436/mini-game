package com.zhiyou.wxgame.ws.listener;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.zhiyou.wxgame.configuration.WebsocketConfig;
import com.zhiyou.wxgame.ws.dispatcher.Dispatcher;
import com.zhiyou.wxgame.ws.websocket.WebSocketServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;

@Component
public class NettyServerListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(NettyServerListener.class);

	private ServerBootstrap serverBootstrap = new ServerBootstrap();
	private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
	private EventLoopGroup workerGroup = new NioEventLoopGroup();

	@Resource
	private WebSocketServerHandler webSocketServerHandler;
	
	public void start() {
		int port = WebsocketConfig.getPort();
		final boolean SSL = System.getProperty("ssl") != null;
		port = Integer.parseInt(System.getProperty("port", SSL? "8443" : String.valueOf(port)));
		Dispatcher.init();
		
		// Configure SSL.
        final SslContext sslCtx;
        try {
	        if (SSL) {
	            SelfSignedCertificate ssc = new SelfSignedCertificate();
	            sslCtx = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
	        } else {
	            sslCtx = null;
	        }
			serverBootstrap.group(bossGroup, workerGroup).
					channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 100)
					.handler(new LoggingHandler(LogLevel.INFO));
		
			// 设置事件处理
			serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline pipeline = ch.pipeline();
					if (sslCtx != null) {
			            pipeline.addLast(sslCtx.newHandler(ch.alloc()));
			        }
					pipeline.addLast(new HttpServerCodec());
			        pipeline.addLast(new HttpObjectAggregator(65536));
			        pipeline.addLast(webSocketServerHandler);
				}
			});
			LOGGER.error("netty服务器在[{}]端口启动监听", port);
			ChannelFuture f = serverBootstrap.bind(port).sync();
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			LOGGER.info("[出现异常] 释放资源");
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

}
