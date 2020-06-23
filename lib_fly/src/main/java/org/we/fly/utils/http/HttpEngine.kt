package org.we.fly.utils.http

import android.util.LruCache
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.google.gson.Gson
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.util.concurrent.TimeUnit

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/23 5:55 PM
 * @description: 网络请求工具
 * @since: 1.0.0
 */
object HttpEngine {
    private const val DEFAULT_CONNECT_TIME = 10L
    private const val DEFAULT_WRITE_TIME = 30L
    private const val DEFAULT_READ_TIME = 30L
    private const val CACHE_SERVICE_COUNT = 100

    private lateinit var defaultRetrofit: Retrofit
    private var defaultOkHttpClient: OkHttpClient
    private var serviceCache: LruCache<String, Any>

    init {
        val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                try {
                    LogUtils.e("OKHttp======>${URLDecoder.decode(message, "utf-8")}")
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                    LogUtils.e("OKHttp======>${message}")
                }
            }
        })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        defaultOkHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(DEFAULT_CONNECT_TIME, TimeUnit.SECONDS)// 连接超时时间
            .writeTimeout(DEFAULT_WRITE_TIME, TimeUnit.SECONDS)// 设置写操作超时时间
            .readTimeout(DEFAULT_READ_TIME, TimeUnit.SECONDS)// 设置读操作超时时间
            .retryOnConnectionFailure(true)
            .cache(
                Cache(
                    File(Utils.getApp().cacheDir.toString() + "HttpCache"),
                    1024L * 1024 * 100
                )
            ).build()
        serviceCache = LruCache(CACHE_SERVICE_COUNT)
    }

    fun buildRetrofit(baseUrl: String) {
        defaultRetrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(defaultOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()
        serviceCache.evictAll()
    }

    fun <T> getService(service: Class<T>): T {
        var retrofitService: T? = serviceCache.get(service.canonicalName) as T
        if (retrofitService == null) {
            retrofitService = defaultRetrofit.create(service)
            serviceCache.put(service.canonicalName, retrofitService)
        }
        return retrofitService!!
    }
}