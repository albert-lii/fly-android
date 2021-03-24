package fly.mod.lib.common.api

import org.we.fly.http.HttpResponse

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/3/19 8:10 PM
 * @description: 网络请求响应通用类
 * @since: 1.0.0
 */
class ApiResponse<T>(code: Int, msg: String, data: T?) : HttpResponse<T>(code, msg, data) {

    override fun isSuccess(): Boolean {
        return code == 0
    }
}