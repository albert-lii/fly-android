package org.fly.base.http

import android.net.ParseException
import android.util.LruCache
import com.google.gson.Gson
import com.google.gson.JsonParseException
import kotlinx.coroutines.CancellationException
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.fly.base.utils.ContextUtils
import org.fly.base.utils.LogUtils
import org.json.JSONException
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.InterruptedIOException
import java.io.UnsupportedEncodingException
import java.net.ConnectException
import java.net.URLDecoder
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/8/19 5:21 PM
 * @description: 基础的Http客户端
 * @since: 1.0.0
 */
abstract class BaseHttpClient {
    open var connectOutTime = 10L // 连接超时时间
    open var writeOutTime = 30L // 设置写操作超时时间
    open var readOutTime = 30L // 设置读操作超时时间
    private val SERVICE_CACHE_COUNT = 20 // 最多缓存的service数量

    private lateinit var serviceCache: LruCache<String, Any>
    private lateinit var okClient: OkHttpClient
    private lateinit var retrofitClient: Retrofit

    /**
     * 初始化
     */
    fun initialize() {
        serviceCache = LruCache(SERVICE_CACHE_COUNT)
        okClient = buildOkHttpClient()
        retrofitClient = buildRetrofitClient()
    }

    open fun buildOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                try {
                    LogUtils.e("FlyOKHttp======>${URLDecoder.decode(message, "utf-8")}")
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                    LogUtils.e("FlyOKHttp======>${message}")
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
            .connectTimeout(connectOutTime, TimeUnit.SECONDS)// 连接超时时间
            .writeTimeout(writeOutTime, TimeUnit.SECONDS)// 设置写操作超时时间
            .readTimeout(readOutTime, TimeUnit.SECONDS)// 设置读操作超时时间
            .retryOnConnectionFailure(true)
            .cache(
                Cache(
                    File(ContextUtils.getApplication().cacheDir.toString() + "FlyHttpCache"),
                    1024L * 1024 * 100
                )
            )
        return builder.build()
    }

    fun buildRetrofitClient(): Retrofit {
        val builder = Retrofit.Builder()
            .baseUrl(getBaseUrl())
            .client(okClient)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
        return builder.build()
    }

    /**
     * 获取service对象
     *
     * @param service api所在接口类
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> getService(service: Class<T>): T {
        var retrofitService: T? = serviceCache.get(service.canonicalName) as T
        if (retrofitService == null) {
            retrofitService = retrofitClient.create(service)
            serviceCache.put(service.canonicalName, retrofitService)
        }
        return retrofitService!!
    }

    /**
     * 建议调用此方法发送网络请求
     * 因为协程中出现异常时，会直接抛出异常，所以使用try...catch方法捕获异常
     */
    suspend fun <T : Any, D : Any> requestSafely(
        apiInterface: Class<T>,
        call: suspend (service: T) -> ResponseWrapper<D>
    ): ParseResult<D> {
        try {
            val s = getService(apiInterface)
            val response = call(s)
            return if (response.isSuccess()) {
                ParseResult.Success(response.data)
            } else {
                ParseResult.Failure(response.code, response.msg)
            }
        } catch (ex: Throwable) {
            return ParseResult.ERROR(ex, parseException(ex))
        }
    }

    /**
     * 解析网络请求异常
     */
    open fun parseException(e: Throwable): HttpError {
        return when (e) {
            is HttpException -> HttpError.BAD_NETWORK
            is ConnectException, is UnknownHostException -> HttpError.CONNECT_ERROR
            is InterruptedIOException -> HttpError.CONNECT_TIMEOUT
            is JsonParseException, is JSONException, is ParseException, is ClassCastException -> HttpError.PARSE_ERROR
            is CancellationException -> HttpError.CANCEL_REQUEST
            else -> HttpError.UNKNOWN
        }
    }

    /**
     * 获取Retrofit实例
     */
    fun getRetrofit(): Retrofit {
        return retrofitClient
    }

    /**
     * 是否处于调试模式
     */
    abstract fun isDebug(): Boolean

    /**
     * 获取Retrofit的BaseUrl
     */
    abstract fun getBaseUrl(): String
}