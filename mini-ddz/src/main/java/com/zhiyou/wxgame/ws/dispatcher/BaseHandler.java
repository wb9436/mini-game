package com.zhiyou.wxgame.ws.dispatcher;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhiyou.wxgame.configuration.WebsocketConfig;
import com.zhiyou.wxgame.util.secret.AES256;
import com.zhiyou.wxgame.util.utils.StringUtils;
import com.zhiyou.wxgame.util.utils.UrlUtils;
import com.zhiyou.wxgame.ws.websocket.DataPacket;
import com.zhiyou.wxgame.ws.websocket.WebSocket;

/**
 * 逻辑处理，每次交互的请求都要实现本接口.
 * 
 */
public abstract class BaseHandler {

	protected final Logger logger = Logger.getLogger(this.getClass());

	protected JSONObject getData(WebSocket webSocket, DataPacket request) {
		String requestData = request.getData();
		JSONObject data = new JSONObject();
		if (requestData != null && !"".equals(requestData)) {
			String text = "";
			if (WebsocketConfig.isAes256()) {
				byte[] bArray = StringUtils.hex2Binary(requestData);
				text = AES256.decode(bArray, StringUtils.getUTFBytes(WebsocketConfig.getAES256Key()));
			} else {
				text = requestData;
			}
			text = UrlUtils.decode(text);
			data = JSON.parseObject(text);
		}
		if (request.getCmd() != 10000) {
			logger.info("Recive msg:cmd=" + request.getCmd() + ";userId=" + webSocket.getUserid() + ";data="
					+ (data != null ? data.toJSONString() : "NULL"));
		}
		return data;
	}

}
