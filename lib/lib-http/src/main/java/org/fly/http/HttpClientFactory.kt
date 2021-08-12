package org.fly.http

import android.content.Context
import okhttp3.OkHttpClient
import org.fly.http.interceptor.HeadersInterceptor
import org.fly.http.interceptor.HttpLoggingInterceptor
import org.fly.http.request.RequestService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/8/19 5:21 PM
 * @description: 基础的Http客户端
 * @since: 1.0.0
 */
open class HttpClientFactory {
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var retrofit: Retrofit
    private lateinit var requestService: RequestService
    private lateinit var config: HttpClientConfig

    /**
     * 初始化
     */
    protected fun initialize(context: Context, httpClientConfig: HttpClientConfig) {
        config = httpClientConfig
        okHttpClient = buildOkHttpClient(context)
        retrofit = buildRetrofit()
        initRequestService()
    }

    /**
     * 构建OkHttp客户端
     */
    open fun buildOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(config.connectTimeout.first, config.connectTimeout.second)
            .readTimeout(config.readTimeout.first, config.readTimeout.second)
            .writeTimeout(config.writeTimeout.first, config.writeTimeout.second)
            .retryOnConnectionFailure(config.retryOnConnectionFailure)
            .cache(config.cache)
            .also { builder ->
                config.headers?.run { builder.addInterceptor(HeadersInterceptor(this)) }
                config.dns?.run { builder.dns(this) }
                config.interceptors.forEach {
                    builder.addInterceptor(it)
                }
                config.netInterceptors.forEach {
                    builder.addNetworkInterceptor(it)
                }
                if (config.openLog) {
                    val logInterceptor = HttpLoggingInterceptor()
                    logInterceptor.level = HttpLoggingInterceptor.Level.BODY
                    builder.addNetworkInterceptor(logInterceptor)
                }
            }
            .build()
    }

    /**
     * 构建Retrofit客户端
     */
    open fun buildRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(config.baseUrl)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(config.gson))
            .build()
    }

    /**
     * 初始化请求Service
     */
    private fun initRequestService() {
        requestService = retrofit.create(RequestService::class.java)
    }

    /**
     * 获取OkHttpClient实例
     */
    fun getOkHttpClient() = okHttpClient

    /**
     * 获取Retrofit实例
     */
    fun getRetrofit() = retrofit

    /**
     * 获取请求Service
     */
    fun getRequestService() = requestService

    /**
     * 获取Gson实例
     */
    fun getGson() = config.gson
}