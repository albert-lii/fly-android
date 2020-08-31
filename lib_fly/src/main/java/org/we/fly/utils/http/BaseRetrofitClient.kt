package org.we.fly.utils.http

import android.util.LruCache
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.google.gson.Gson
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.util.concurrent.TimeUnit

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/8/19 5:21 PM
 * @description: 基础的Retrofit客户端
 * @since: 1.0.0
 */
abstract class BaseRetrofitClient {
    companion object {
        private const val DEFAULT_CONNECT_TIME = 10L // 连接超时时间
        private const val DEFAULT_WRITE_TIME = 30L // 设置写操作超时时间
        private const val DEFAULT_READ_TIME = 30L // 设置读操作超时时间
        private const val SERVICE_CACHE_COUNT = 20 // 最多缓存的service数量
    }

    private var serviceCache: LruCache<String, Any>
    protected val okClient: OkHttpClient
    protected val retrofit: Retrofit

    init {
        serviceCache = LruCache(SERVICE_CACHE_COUNT)
        okClient = buildOkHttpClient()
        retrofit = buildRetrofit()
    }

    private fun buildOkHttpClient(): OkHttpClient {
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
        if (isDebug()) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        }
        val builder = OkHttpClient.Builder()
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
            )
        handleOkBuilder(builder)
        return builder.build()
    }

    private fun buildRetrofit(): Retrofit {
        val builder = Retrofit.Builder()
            .baseUrl(getBaseUrl())
            .client(okClient)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
        handleRetrofitBuilder(builder)
        return builder.build()
    }


    /**
     * 获取service对象
     *
     * @param service api所在接口类
     */
    fun <T> getService(service: Class<T>): T {
        var retrofitService: T? = serviceCache.get(service.canonicalName) as T
        if (retrofitService == null) {
            retrofitService = retrofit.create(service)
            serviceCache.put(service.canonicalName, retrofitService)
        }
        return retrofitService!!
    }

    abstract fun isDebug(): Boolean

    /**
     * 获取Retrofit的BaseUrl
     */
    abstract fun getBaseUrl(): String

    /**
     * 如需对OKHttpClient做额外处理，可在此方法中进行
     */
    abstract fun handleOkBuilder(builder: OkHttpClient.Builder)

    /**
     * 如需对Retrofit做额外处理，可在此方法中进行
     */
    abstract fun handleRetrofitBuilder(builder: Retrofit.Builder)
}