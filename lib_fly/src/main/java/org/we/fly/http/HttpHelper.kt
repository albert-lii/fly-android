package org.we.fly.http

import android.net.ParseException
import com.blankj.utilcode.util.GsonUtils
import com.google.gson.JsonParseException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/8/18 5:55 PM
 * @description: 网络请求帮助类
 * @since: 1.0.0
 */
interface HttpHelper {

    /**
     * 将对象类转换为RequestBody
     */
    fun convertRequesBody(obj: Any): RequestBody {
        return GsonUtils.toJson(obj).toRequestBody("application/json".toMediaTypeOrNull())
    }
}