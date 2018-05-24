package com.zhiyou.wxgame.util.secret;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AES {

	private static final String KEY_ALGORITHM = "AES";
	private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";
	protected static final String AES_ALGORITHM1 = "AES/ECB/PKCS5Padding";
	
	private static final String CHARSET_UTF8 = "UTF-8";

	public static String encrypt(String password, String data) throws Exception {
		SecretKeySpec skeySpec = getKey(password);
		Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
//		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, getIvSpec());
		byte[] encrypted = cipher.doFinal(data.getBytes(CHARSET_UTF8));
		return new BASE64Encoder().encode(encrypted);
	}

	public static String decrypt(String password, String data) throws Exception {
		SecretKeySpec skeySpec = getKey(password);
		Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
//		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, getIvSpec());
		byte[] encrypted1 = new BASE64Decoder().decodeBuffer(data);
		byte[] original = cipher.doFinal(encrypted1);
		String originalString = new String(original,CHARSET_UTF8);
		return originalString;
	}

	/**
	 * @deprecated
	 * @param password
	 * @return
	 * @throws Exception
	 */
	protected static SecretKeySpec getKey0(String password) throws Exception {
		byte[] arrBTmp = password.getBytes();
		byte[] arrB = new byte[16]; // 创建一个空的16位字节数组（默认值为0）
		for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
			arrB[i] = arrBTmp[i];
		}
		KeyGenerator kgen = KeyGenerator.getInstance(KEY_ALGORITHM);
		kgen.init(128, new SecureRandom(password.getBytes(CHARSET_UTF8)));
		SecretKey secretKey = kgen.generateKey();
		byte[] enCodeFormat = secretKey.getEncoded();
		return new SecretKeySpec(enCodeFormat, KEY_ALGORITHM);
	}

	private static SecretKeySpec getKey(String password) throws Exception {
		byte[] arrBTmp = password.getBytes(CHARSET_UTF8);
		byte[] arrB = new byte[16]; // 创建一个空的16位字节数组（默认值为0）
		for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
			arrB[i] = arrBTmp[i];
		}
//		SecretKeySpec spec = new SecretKeySpec(arrBTmp,KEY_ALGORITHM);
//		byte[] encodedFormt = spec.getEncoded();
		return new SecretKeySpec(arrB, KEY_ALGORITHM);
	}

	public static IvParameterSpec getIvSpec() {
		return new IvParameterSpec("0102030405060708".getBytes());
	}

}
