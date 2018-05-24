// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DesUtils.java

package com.zhiyou.wxgame.util.secret;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DesSecret {

	private String desKEY;

	public DesSecret() {
	}

	public DesSecret(String key) {
		this.desKEY = key;
	}

	public String getDesKEY() {
		return desKEY;
	}

	public void setDesKEY(String desKEY) {
		this.desKEY = desKEY;
	}

	/**
	 * 加密
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public String encrypt(String message) throws Exception {
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(desKEY.getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		javax.crypto.SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(desKEY.getBytes("UTF-8"));
		cipher.init(1, secretKey, iv);
		return toHexString(cipher.doFinal(message.getBytes("UTF-8")));
	}

	/**
	 * 解密
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public String decrypt(String message) throws Exception {
		byte bytesrc[] = convertHexString(message);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(desKEY.getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		javax.crypto.SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(desKEY.getBytes("UTF-8"));
		cipher.init(2, secretKey, iv);
		byte retByte[] = cipher.doFinal(bytesrc);
		return new String(retByte);
	}

	private byte[] convertHexString(String ss) {
		byte digest[] = new byte[ss.length() / 2];
		for (int i = 0; i < digest.length; i++) {
			String byteString = ss.substring(2 * i, 2 * i + 2);
			int byteValue = Integer.parseInt(byteString, 16);
			digest[i] = (byte) byteValue;
		}

		return digest;
	}

	private String toHexString(byte b[]) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String plainText = Integer.toHexString(0xff & b[i]);
			if (plainText.length() < 2)
				plainText = (new StringBuilder("0")).append(plainText).toString();
			hexString.append(plainText);
		}

		return hexString.toString();
	}

}
