package org.fly.utils

import android.util.Base64
import java.security.MessageDigest

object SecurityUtils {

    /**
     * MD5加密字符串
     * 原始加密数据为16字节，转16进制后32字节
     */
    fun md5(text: String) = encode(text, "MD5")

    /**
     * SHA1加密字符串
     * 原始加密数据为20字节，转16进制后40字节
     */
    fun sha1(text: String) = encode(text, "SHA-1")

    /**
     * SHA256加密字符串
     * 原始加密数据为32字节，转16进制后64字节
     */
    fun sha256(text: String) = encode(text, "SHA-256")

    @JvmStatic
    fun encode(text: String, algorithm: String): String {
        val digest = MessageDigest.getInstance(algorithm)
        val result = digest.digest(text.toByteArray())
        return toHex(result)
    }

    /**
     * 转16进制
     */
    @JvmStatic
    private fun toHex(byteArray: ByteArray): String {
        val result = with(StringBuilder()) {
            byteArray.forEach {
                val hex = it.toInt() and (0xFF)
                val hexStr = Integer.toHexString(hex)
                if (hexStr.length == 1) {
                    // 如果是一位的话，补0
                    this.append("0").append(hexStr)
                } else {
                    this.append(hexStr)
                }
            }
            this.toString()
        }
        return result
    }

    /**
     * Base64加密
     */
    @JvmStatic
    fun base64Encode(data: ByteArray?): String {
        return Base64.encodeToString(data, Base64.DEFAULT).replace("\n", "")
    }

    /**
     * Base64解密
     */
    @JvmStatic
    fun base64Decode(data: String): ByteArray {
        return Base64.decode(data.toByteArray(), Base64.DEFAULT)
    }
}