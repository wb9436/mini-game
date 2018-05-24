package com.zhiyou.wxgame.ws.websocket;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhiyou.wxgame.Enum.Errors;
import com.zhiyou.wxgame.configuration.WebsocketConfig;
import com.zhiyou.wxgame.util.secret.AES256;
import com.zhiyou.wxgame.util.utils.StringUtils;
import com.zhiyou.wxgame.util.utils.UrlUtils;

public class DataPacket {
	protected final Logger logger = Logger.getLogger(getClass());
	
	private int cmd;
	private String data;
	private long seq;

	public DataPacket packet(JSONObject json) {
		String str = UrlUtils.encode(json.toJSONString());
		if (WebsocketConfig.isAes256()) {
			byte[] bArray = AES256.encode(str, StringUtils.getUTFBytes(WebsocketConfig.getAES256Key()));
			this.data = StringUtils.binary2Hex(bArray);
		} else {
			this.data = str;
		}
		return this;
	}

	public DataPacket() {
	}

	public DataPacket(int cmd) {
		this.cmd = cmd;
	}

	public DataPacket(int cmd, Errors err) {
		this.cmd = cmd;
		JSONObject json = new JSONObject();
		json.put("code", err.getCode());
		json.put("msg", err.getMsg());
		String str = UrlUtils.encode(json.toJSONString());
		if (WebsocketConfig.isAes256()) {
			byte[] bArray = AES256.encode(str, StringUtils.getUTFBytes(WebsocketConfig.getAES256Key()));
			this.data = StringUtils.binary2Hex(bArray);
		} else {
			this.data = str;
		}
	}

	public DataPacket(int cmd, JSONObject json) {
		this.cmd = cmd;
		json.put("code", Errors.OK.getCode());
		json.put("msg", Errors.OK.getMsg());
		logger.info("cmd=" + cmd + ";" + json.toJSONString());
		String str = UrlUtils.encode(json.toJSONString());
		if (WebsocketConfig.isAes256()) {
			byte[] bArray = AES256.encode(str, StringUtils.getUTFBytes(WebsocketConfig.getAES256Key()));
			this.data = StringUtils.binary2Hex(bArray);
		} else {
			this.data = str;
		}
	}

	public DataPacket(int cmd, Errors err, JSONArray jsonArray) {
		this.cmd = cmd;
		JSONObject json = new JSONObject();
		json.put("code", err.getCode());
		json.put("msg", err.getMsg());
		json.put("array", jsonArray);
		String str = UrlUtils.encode(jsonArray.toJSONString());
		if (WebsocketConfig.isAes256()) {
			byte[] bArray = AES256.encode(str, StringUtils.getUTFBytes(WebsocketConfig.getAES256Key()));
			this.data = StringUtils.binary2Hex(bArray);
		} else {
			this.data = str;
		}
	}

	public int getCmd() {
		return cmd;
	}

	public void setCmd(int cmd) {
		this.cmd = cmd;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "DataPacket [cmd=" + cmd + ", data=" + data + ", seq=" + seq + "]";
	}

	public long getSeq() {
		return seq;
	}

	public void setSeq(long seq) {
		this.seq = seq;
	}

}
