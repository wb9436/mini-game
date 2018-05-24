package com.zhiyou.wxgame.ws.dispatcher;

import java.util.HashMap;

import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

/**
 * 逻辑转发，游戏启动前，先注册逻辑处理类
 * 
 * WB
 */
public class Dispatcher {

	public static HashMap<Integer, String> localMap = new HashMap<Integer, String>();
	protected static final Logger logger = Logger.getLogger(Dispatcher.class);

	public static void register(Integer cmd, String handlerName) {
		localMap.put(cmd, handlerName);
	}

	public static String getHandlerClass(Integer command) {
		return localMap.get(command);
	}
	
	public static void init() {
		try {
			XMLConfiguration config = new XMLConfiguration("handler.xml");
			int size = config.getList("handler.cmd").size();
			if(size > 0){
				for(int i = 0; i < size; i++){
					int cmd = config.getInt("handler(" + i + ").cmd");
					String handlerClass = config.getString("handler(" + i + ").handlerClass");
					String desc = config.getString("handler(" + i + ").desc");
					logger.info("Load handler: request cmd:" + cmd + "; class is:" + handlerClass + "; desc:" + desc);
					register(cmd, handlerClass);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
