package fly.we.app

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import dagger.hilt.android.HiltAndroidApp
import fly.lib.common.http.FlyHttp
import org.fly.base.utils.AppUtils

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/3/17 7:11 PM
 * @description: -
 * @since: 1.0.0
 */
@HiltAndroidApp
class FlyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
        AppUtils.init(this)
        FlyHttp.getInstance().init(this)
    }
}