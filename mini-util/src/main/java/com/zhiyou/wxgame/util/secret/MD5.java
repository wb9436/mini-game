package com.zhiyou.wxgame.util.secret;

import java.security.MessageDigest;

public class MD5 {

	public static final String CHARSET_UTF8 = "UTF-8";
	public static final String CHARSET_GBK = "GBK";

	public static String encode(String str, String charset) {
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(str.getBytes(charset));
			byte bytes[] = m.digest();
			return toHex(bytes);
		} catch (Exception e) {
		}
		return "";
	}

	public static String encodeUTF8(String str) {
		return encode(str, CHARSET_UTF8);
	}

	public static String encodeGBK(String str) {
		return encode(str, CHARSET_GBK);
	}

	public static String toHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			if ((bytes[i] & 0xff) < 0x10) {
				sb.append("0");
			}
			sb.append(Integer.toString(bytes[i] & 0xff, 16));
		}
		return sb.toString();
	}

}
