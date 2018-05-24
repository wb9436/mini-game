/**
 * ByteUtils.java
 * griffin
 * 2011-3-14
 */
package com.zhiyou.wxgame.util.secret;

import java.io.UnsupportedEncodingException;

/**
 *
 */
public class ByteUtils {
	
	public static byte[] short2Bytes(short i) {
		byte[] bytes = new byte[2];
		bytes[0] = (byte)(i>>>8);
		bytes[1] = (byte)i;
		return bytes;
	}
	
	public static short bytes2Short(byte[] b) {
		int i = b[0]<< 8; 
		i |= b[1];
		return (short)i;
	}
	
	public static byte[] int2Bytes(int i) {
		byte[] bytes = new byte[4];
		for (int index = 0; index < bytes.length; index ++) {
			bytes[index] = (byte)((i >>> (24 - index * 8)) &0xff);
		}
	    return bytes;
	}
	
	public static int bytes2Int(byte[] bytes) {
		int mask = 0xff;
		int i = 0;
		for (int index = 0; index < 4; index++) {
			i <<= 8;
			i |= bytes[index] & mask;
		}
		return i;
	}
	
	public static byte[] gbk2Bytes(String str) {
		try {
			return str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			return new byte[0];
		}
	}
	public static byte[] utf2Bytes(String str) {
		try {
			return str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			return new byte[0];
		}
	}
	
	public static String bytes2UTFString(byte[] b) {
		try {
			return new String(b,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	/**
     * 将16进制字符转换为byte数组
     * @param str
     * @return
     * @author TanGuiyuan
     * @date 2013-5-4
     */
    public static byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(
                        str.substring(i * 2, i * 2 + 2), 16);
            }
            return buffer;
        }
    }

    /**
     * 将byte数组转为16进制字符串
     * @param b
     * @return
     * @author TanGuiyuan
     * @date 2013-5-4
     */
    public static String bytes2Hex(byte[] b) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String tmp = Integer.toHexString(b[i] & 0xFF);
            if (tmp.length() == 1) {
                buf.append("0").append(tmp);
            } else {
                buf.append(tmp);
            }
        }
        return buf.toString();
    }

}
