package com.zhiyou.wxgame;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.zhiyou.wxgame.ws.listener.NettyServerListener;

@SpringBootApplication
public class MiniDdzApplication implements CommandLineRunner  {

	@Resource
	private NettyServerListener nettyServerListener;
	
	public static void main(String[] args) {
		SpringApplication.run(MiniDdzApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		nettyServerListener.start();
	}
	
}
