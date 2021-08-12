package fly.ly.app

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import fly.mod.lib.common.api.ApiClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.fly.base.utils.AppUtils

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/3/17 7:11 PM
 * @description: -
 * @since: 1.0.0
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
        AppUtils.init(this)
        ApiClient.getInstance().init(this)
    }
}