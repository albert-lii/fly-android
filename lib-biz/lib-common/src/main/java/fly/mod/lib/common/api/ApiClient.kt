package fly.mod.lib.common.api

import org.fly.base.http.BaseHttpClient

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/3/19 8:10 PM
 * @description: -
 * @since: 1.0.0
 */
class ApiClient : BaseHttpClient() {

    companion object {
        @Volatile
        private var instance: ApiClient? = null

        @JvmStatic
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: ApiClient().also { instance = it }
            }
    }

    override fun getBaseUrl(): String {
        return "https://www.liyisite.com/v1/"
    }

    override fun isDebug(): Boolean {
        return true
    }
}