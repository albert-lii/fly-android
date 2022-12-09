package org.fly.http.ssl;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密工具类
 */
public class EncryptUtil {
    private static String algorithm = "AES/CBC/PKCS5Padding";

    /**
     * AES加密
     */
    public static String encryptWithAES(String key, String cleartext) {
        try {
            // 两个参数，第一个为私钥字节数组， 第二个为加密方式 AES或者DES
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
            //实例化加密类，参数为加密方式，要写全
            Cipher cipher = Cipher.getInstance(algorithm);
            //初始化，此方法可以采用三种方式，按加密算法要求来添加。（1）无第三个参数（2）第三个参数为SecureRandom random = new SecureRandom();中random对象，随机数。(AES不可采用这种方法)（3）采用此代码中的IVParameterSpec
            //加密时使用:ENCRYPT_MODE;  解密时使用:DECRYPT_MODE;
            IvParameterSpec iv = new IvParameterSpec(key.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            // 加密操作,返回加密后的字节数组，然后需要编码。主要编解码方式有Base64, HEX, UUE,7bit等等。此处看服务器需要什么编码方式
            byte[] encryptedData = cipher.doFinal(cleartext.getBytes());
            return Base64.encodeToString(encryptedData, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * AES解密
     */
    public static String decryptWithAES(String key, String ivs,String enData) {
        try {
            String encodingFormat = "UTF8";
            String algorithm = "AES/CBC/PKCS5Padding";
            byte[] raw = key.getBytes(encodingFormat);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(algorithm);
            IvParameterSpec iv = new IvParameterSpec(ivs.getBytes(encodingFormat));
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            // 先用base64解密
            byte[] encrypted = Base64.decode(enData, Base64.DEFAULT);
            byte[] original = cipher.doFinal(encrypted);
            return new String(original, encodingFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
