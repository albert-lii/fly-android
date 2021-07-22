package org.fly.base.utils

import android.content.Context
import android.os.Parcelable
import com.getkeepsafe.relinker.ReLinker
import com.tencent.mmkv.MMKV
import java.util.*

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/5/29 10:34 PM
 * @description: 基于MMKV的本地缓存
 * @since: 1.0.0
 */
class LocalCache private constructor() {

    companion object {
        @Volatile
        private var instance: LocalCache? = null

        @JvmStatic
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: LocalCache().also { instance = it }
            }

        @JvmField
        var cryptKey = "fly-android"

        @JvmStatic
        fun initialize(context: Context) {
            // 一些 Android 设备（API level 19）在安装/更新 APK 时可能出错, 导致 libmmkv.so 找不到
            MMKV.initialize(context) { libName -> ReLinker.loadLibrary(context, libName) }
        }

        @JvmStatic
        fun initialize(context: Context, rootDir: String) {
            // 一些 Android 设备（API level 19）在安装/更新 APK 时可能出错, 导致 libmmkv.so 找不到
            MMKV.initialize(context, rootDir) { libName -> ReLinker.loadLibrary(context, libName) }
        }
    }

    var cache: MMKV?

    init {
        cache = MMKV.defaultMMKV(MMKV.SINGLE_PROCESS_MODE, cryptKey)
    }

    fun put(key: String, value: Any?): Boolean {
        if (cache == null) {
            return false
        }
        when (value) {
            is String -> return cache!!.encode(key, value)
            is Float -> return cache!!.encode(key, value)
            is Boolean -> return cache!!.encode(key, value)
            is Int -> return cache!!.encode(key, value)
            is Long -> return cache!!.encode(key, value)
            is Double -> return cache!!.encode(key, value)
            is ByteArray -> return cache!!.encode(key, value)
        }
        return false
    }

    fun <T : Parcelable> put(key: String, t: T?): Boolean {
        return cache?.encode(key, t) ?: false
    }

    fun put(key: String, sets: Set<String>?): Boolean {
        return cache?.encode(key, sets) ?: false
    }

    fun getInt(key: String): Int? {
        return cache?.decodeInt(key, 0)
    }

    fun getDouble(key: String): Double? {
        return cache?.decodeDouble(key, 0.00)
    }

    fun getLong(key: String): Long? {
        return cache?.decodeLong(key, 0L)
    }

    fun getBoolean(key: String): Boolean? {
        return cache?.decodeBool(key, false)
    }

    fun getFloat(key: String): Float? {
        return cache?.decodeFloat(key, 0F)
    }

    fun getString(key: String): String? {
        return cache?.decodeString(key, "")
    }

    fun getBytes(key: String): ByteArray? {
        return cache?.decodeBytes(key)
    }

    fun <T : Parcelable> getParcelable(key: String, tClass: Class<T>): T? {
        return cache?.decodeParcelable(key, tClass)
    }

    fun getStringSet(key: String): Set<String>? {
        return cache?.decodeStringSet(key, Collections.emptySet())
    }

    fun removeKey(key: String) {
        cache?.removeValueForKey(key)
    }

    fun removeKeys(keys: Array<String>) {
        cache?.removeValuesForKeys(keys)
    }

    fun clearAll() {
        cache?.clearAll()
    }

    fun isKeyExisted(key: String): Boolean {
        return cache?.containsKey(key) ?: false
    }

    fun getTotalSize(): Long {
        return cache?.totalSize() ?: 0
    }

    fun encrypted(isEncrypted: Boolean) {
        cache?.let {
            if (isEncrypted) {
                // 加密
                it.reKey(cryptKey)
            } else {
                // 明文
                it.reKey(null)
            }
        }
    }
}