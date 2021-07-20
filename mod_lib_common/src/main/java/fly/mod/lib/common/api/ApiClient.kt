package fly.mod.lib.common.api

import okhttp3.OkHttpClient
import org.fly.base.network.BaseHttpClient
import retrofit2.Retrofit

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
        return "https://www.liyisite.com/api/v1/"
    }

    override fun isDebug(): Boolean {
        return true
    }

    override fun handleOkBuilder(builder: OkHttpClient.Builder) {

    }

    override fun handleRetrofitBuilder(builder: Retrofit.Builder) {

    }
}