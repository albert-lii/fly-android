package org.fly.utils.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.Reader
import java.lang.reflect.Type

/**
 * Gson相关工具类
 */
object GsonUtils {
    private var gson: Gson? = null

    @JvmStatic
    fun getGson(): Gson {
        if (gson == null) {
            gson = GsonBuilder()
                // 遇到Map类型时，使用我们自己的Adapter
                .registerTypeAdapter(
                    object : TypeToken<Map<String?, Any?>?>() {}.type,
                    MapTypeAdapter()
                )
                .create()
        }
        return gson!!
    }

    @JvmStatic
    fun toJson(`object`: Any?): String? {
        return toJson(getGson(), `object`)
    }

    @JvmStatic
    fun toJson(src: Any?, typeOfSrc: Type): String? {
        return toJson(getGson(), src, typeOfSrc)
    }

    @JvmStatic
    fun toJson(gson: Gson, `object`: Any?): String? {
        return gson.toJson(`object`)
    }

    @JvmStatic
    fun toJson(gson: Gson, src: Any?, typeOfSrc: Type): String? {
        return gson.toJson(src, typeOfSrc)
    }

    @JvmStatic
    fun <T> fromJson(json: String?, type: Class<T>): T {
        return fromJson(getGson(), json, type) as T
    }

    @JvmStatic
    fun <T> fromJson(json: String?, type: Type): T {
        return fromJson(getGson(), json, type)
    }

    @JvmStatic
    fun <T> fromJson(reader: Reader, type: Class<T>): T {
        return fromJson(getGson(), reader, type)
    }

    @JvmStatic
    fun <T> fromJson(reader: Reader, type: Type): T {
        return fromJson(getGson(), reader, type)
    }

    @JvmStatic
    fun <T> fromJson(gson: Gson, json: String?, type: Class<T>): T {
        return gson.fromJson(json, type)
    }

    @JvmStatic
    fun <T> fromJson(gson: Gson, json: String?, type: Type): T {
        return gson.fromJson(json, type)
    }

    @JvmStatic
    fun <T> fromJson(gson: Gson, reader: Reader?, type: Class<T>): T {
        return gson.fromJson(reader, type)
    }

    @JvmStatic
    fun <T> fromJson(gson: Gson, reader: Reader?, type: Type): T {
        return gson.fromJson(reader, type)
    }

    @JvmStatic
    fun getListType(type: Type): Type {
        return TypeToken.getParameterized(MutableList::class.java, type).type
    }

    @JvmStatic
    fun getSetType(type: Type): Type {
        return TypeToken.getParameterized(MutableSet::class.java, type).type
    }

    @JvmStatic
    fun getMapType(keyType: Type, valueType: Type): Type {
        return TypeToken.getParameterized(MutableMap::class.java, keyType, valueType).type
    }

    @JvmStatic
    fun getArrayType(type: Type): Type {
        return TypeToken.getArray(type).type
    }

    @JvmStatic
    fun getType(rawType: Type, vararg typeArguments: Type): Type {
        return TypeToken.getParameterized(rawType, *typeArguments).type
    }
}