package org.fly.http.response

import android.net.ParseException
import com.google.gson.JsonParseException
import org.json.JSONException
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/23 5:31 PM
 * @description: 解析后的网络请求异常信息类
 * @since: 1.0.0
 */
open class HttpError(
    var httpCode: Int? = null,
    var errorCode: Int,
    var errorMsg: String,
    var cause: Throwable? = null
) {

    companion object {
        /**
         * 异常错误码
         */
        const val UNKNOW_ERROR = -1
        const val CANCEL_REQUEST = -2
        const val CONNECT_ERROR = -3
        const val CONNECT_TIMEOUT = -4
        const val BAD_NETWORK = -5
        const val PARSE_ERROR = -6
    }
}
