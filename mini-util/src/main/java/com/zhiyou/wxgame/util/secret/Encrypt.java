package com.zhiyou.wxgame.util.secret;

/**
 * 简单加密算法，（XOR）
 * 
 * @author guiyuan
 * 
 */
public class Encrypt {

    /**
     * 加密，解密方法
     * 
     * @param content
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] content, String key) {
        final String DEFAULT_KEY = "q0m3sd81";
        byte[] byteArray = content;
        try {
            int len = content.length;
            byte[] byteKey = key.getBytes("utf-8");
            int klen = byteKey.length;
            if (klen == 0) {
                byteKey = DEFAULT_KEY.getBytes("utf-8");
            }
            int t = 0;
            for (int i = 0; i < len; i++) {
                t = i % klen;
                byteArray[i] = (byte) (byteArray[i] ^ byteKey[t]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteArray;
    }

}
