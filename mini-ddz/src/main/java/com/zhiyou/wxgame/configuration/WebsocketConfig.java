package com.zhiyou.wxgame.configuration;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Netty Socket 服务器配置信息
 */
@Configuration
public class WebsocketConfig implements ApplicationContextAware {
	private static Environment env;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		env = applicationContext.getBean(Environment.class);
	}

	public static String getValue(String key) {
		return env.getProperty(key);
	}

	public static String getAES256Key() {
		return getValue("wxgame.socket.AES256Key");
	}

	public static String getMD5Key() {
		return getValue("wxgame.socket.MD5Key");
	}

	public static boolean isAes256() {
		return Boolean.parseBoolean(getValue("wxgame.socket.isAes256"));
	}

	public static int getTimeout() {
		return Integer.parseInt(getValue("wxgame.socket.timeout"));
	}

	public static int getIntervalTime() {
		return Integer.parseInt(getValue("wxgame.socket.intervalTime"));
	}

	public static int getPort() {
		return Integer.parseInt(getValue(Constants.KEY_PORT));
	}

	public static String getAddr() {
		return getValue(Constants.KEY_ADDR);
	}

	
}
