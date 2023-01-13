package org.fly.utils

import android.content.Context
import android.os.Parcelable
import com.getkeepsafe.relinker.ReLinker
import com.tencent.mmkv.MMKV


/**
 * 轻量易用的本地缓存工具类
 */
class QuickStore private constructor() {

    companion object {
        @Volatile
        private var instance: QuickStore? = null

        @JvmStatic
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: QuickStore().also { instance = it }
            }

        @JvmStatic
        fun initialize(context: Context) {
            // 一些Android设备（API level 19）在安装/更新APK时可能出错, 导致libmmkv.so找不到
            // 需使用ReLinker加载MMKV可解决此问题
            MMKV.initialize(context) { libName -> ReLinker.loadLibrary(context, libName) }
        }

        @JvmStatic
        fun initialize(context: Context, rootDir: String) {
            // 一些 Android 设备（API level 19）在安装/更新 APK 时可能出错, 导致 libmmkv.so 找不到
            MMKV.initialize(context, rootDir) { libName -> ReLinker.loadLibrary(context, libName) }
        }
    }

    // 数据加密使用的Key
    private val cryptKey = "CloudCrypto"

    private var mmkvObj: MMKV? = MMKV.mmkvWithID("CloudCrypto", 1, cryptKey)

    /**
     * 存储数据
     */
    fun put(key: String, value: Any?): Boolean {
        mmkvObj?.let {
            when (value) {
                is String -> return it.encode(key, value)
                is Boolean -> return it.encode(key, value)
                is Int -> return it.encode(key, value)
                is Long -> return it.encode(key, value)
                is Float -> return it.encode(key, value)
                is Double -> return it.encode(key, value)
                is ByteArray -> return it.encode(key, value)
                else -> return false
            }
        }
        return false
    }

    fun <T : Parcelable> put(key: String, t: T?): Boolean {
        return mmkvObj?.encode(key, t) ?: false
    }

    fun put(key: String, sets: Set<String>?): Boolean {
        return mmkvObj?.encode(key, sets) ?: false
    }

    fun getString(key: String, defValue: String? = null): String? {
        return mmkvObj?.decodeString(key, defValue)
    }

    fun getBoolean(key: String, defValue: Boolean = false): Boolean {
        return mmkvObj?.decodeBool(key, defValue) ?: defValue
    }

    fun getInt(key: String, defValue: Int = 0): Int {
        return mmkvObj?.decodeInt(key, defValue) ?: defValue
    }

    fun getLong(key: String, defValue: Long = 0L): Long {
        return mmkvObj?.decodeLong(key, defValue) ?: defValue
    }

    fun getFloat(key: String, defValue: Float = 0F): Float {
        return mmkvObj?.decodeFloat(key, 0F) ?: defValue
    }

    fun getDouble(key: String, defValue: Double = 0.00): Double {
        return mmkvObj?.decodeDouble(key, defValue) ?: defValue
    }

    fun getBytes(key: String, defValue: ByteArray? = null): ByteArray? {
        return mmkvObj?.decodeBytes(key, defValue)
    }

    fun <T : Parcelable> getParcelable(key: String, tClass: Class<T>, defValue: T? = null): T? {
        return mmkvObj?.decodeParcelable(key, tClass, defValue)
    }

    fun getStringSet(key: String, defValue: Set<String>? = null): Set<String>? {
        return mmkvObj?.decodeStringSet(key, defValue)
    }

    fun remove(key: String) {
        mmkvObj?.removeValueForKey(key)
    }

    fun remove(keys: Array<String>) {
        mmkvObj?.removeValuesForKeys(keys)
    }

    fun clear() {
        mmkvObj?.clearAll()
    }

    fun isKeyExisted(key: String): Boolean {
        return mmkvObj?.containsKey(key) ?: false
    }

    fun getTotalSize(): Long {
        return mmkvObj?.totalSize() ?: 0
    }

    /**
     * 设置加密状态
     */
    fun setCryptoStatus(isEncrypted: Boolean) {
        mmkvObj?.let {
            if (isEncrypted) {
                // 加密
                it.reKey(cryptKey)
            } else {
                // 明文
                it.reKey(null)
            }
        }
    }

    fun getCacheObject(): MMKV? {
        return mmkvObj
    }
}