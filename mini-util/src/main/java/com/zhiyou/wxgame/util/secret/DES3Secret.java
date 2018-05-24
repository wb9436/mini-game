package com.zhiyou.wxgame.util.secret;

import java.security.Security;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

/**
 * key:必须为16位或者24位
 * 
 * @author TanGuiyuan
 * @date 2013-4-11
 */
public class DES3Secret {
    private final String TriDes = "DESede/ECB/NoPadding";

    private byte[] keyBytes = null;

    public DES3Secret(String key) {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        this.keyBytes = key.getBytes();
    }

    public DES3Secret(String key1, String key2, String key3) {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        this.keyBytes = (key1 + key2 + key3).getBytes();
    }

    /**
     * 加密处理
     * 
     * @param encryptValue
     * @return
     * @throws Exception
     * @author TanGuiyuan
     * @date 2013-4-11
     */
    public byte[] encrypt(byte[] encryptValue) throws Exception {
        return this.trides_crypt(this.keyBytes, encryptValue);
    }
    
    /***
     * 加密处理
     * @param str
     * @return
     * @throws Exception
     * @author LuZhiYong
     * @Date 2013-4-16
     */
    public String encrypt(String str) throws Exception{
        if(str == null || str.trim().isEmpty()){
            throw new RuntimeException("The encrypted string cannot be empty.");
        }
        byte[] bytes = encrypt(str.getBytes());
        return ByteUtils.bytes2Hex(bytes);
    }
    
    /****
     * 解密处理
     * @param str
     * @return
     * @throws Exception
     * @author LuZhiYong
     * @Date 2013-4-16
     */
    public String decrypt(String str) throws Exception{
        if(str == null || str.trim().isEmpty()){
            throw new RuntimeException("The decrypted string cannot be empty.");
        }
        byte[] bytes = ByteUtils.hexToBytes(str);
        return new String(decrypt0(bytes));
    }

    /**
     * 解密
     * 
     * @param decryptValue
     * @return
     * @throws Exception
     * @author TanGuiyuan
     * @date 2013-4-11
     */
    public byte[] decrypt(byte[] decryptValue) throws Exception {
        return this.trides_decrypt(this.keyBytes, decryptValue);
    }

    /**
     * 解密后对补位情况处理.
     * 
     * @param decryptValue
     * @return
     * @throws Exception
     * @author TanGuiyuan
     * @date 2013-4-11
     */
    public byte[] decrypt0(byte[] decryptValue) throws Exception {
        byte[] result = this.trides_decrypt(this.keyBytes, decryptValue);
        int i = result.length - 1;
        for (; i >= 0; i--) {
            if (0x00 != result[i]) {
                break;
            }
        }
        byte[] dest = new byte[i + 1];
        System.arraycopy(result, 0, dest, 0, dest.length);
        return dest;
    }

    /**
     * 加密
     * 
     * @param key
     * @param data
     * @return
     * @author TanGuiyuan
     * @date 2013-4-11
     */
    public byte[] trides_crypt(byte key[], byte data[]) {
        try {
            byte[] k = new byte[24];

            int len = data.length;
            if (data.length % 8 != 0) {
                len = data.length - data.length % 8 + 8;
            }
            byte[] needData = null;
            if (len != 0)
                needData = new byte[len];

            for (int i = 0; i < len; i++) {
                needData[i] = 0x00;
            }

            System.arraycopy(data, 0, needData, 0, data.length);

            if (key.length == 16) {
                System.arraycopy(key, 0, k, 0, key.length);
                System.arraycopy(key, 0, k, 16, 8);
            } else {
                System.arraycopy(key, 0, k, 0, 24);
            }

            KeySpec ks = new DESedeKeySpec(k);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");
            SecretKey ky = kf.generateSecret(ks);

            Cipher c = Cipher.getInstance(TriDes);
            c.init(Cipher.ENCRYPT_MODE, ky);
            return c.doFinal(needData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 解密
     * 
     * @param key
     * @param data
     * @return
     * @author TanGuiyuan
     * @date 2013-4-11
     */
    public byte[] trides_decrypt(byte key[], byte data[]) {
        try {
            byte[] k = new byte[24];

            int len = data.length;
            if (data.length % 8 != 0) {
                len = data.length - data.length % 8 + 8;
            }
            byte[] needData = null;
            if (len != 0)
                needData = new byte[len];

            for (int i = 0; i < len; i++) {
                needData[i] = 0x00;
            }

            System.arraycopy(data, 0, needData, 0, data.length);

            if (key.length == 16) {
                System.arraycopy(key, 0, k, 0, key.length);
                System.arraycopy(key, 0, k, 16, 8);
            } else {
                System.arraycopy(key, 0, k, 0, 24);
            }
            KeySpec ks = new DESedeKeySpec(k);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");
            SecretKey ky = kf.generateSecret(ks);

            Cipher c = Cipher.getInstance(TriDes);
            c.init(Cipher.DECRYPT_MODE, ky);
            return c.doFinal(needData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}