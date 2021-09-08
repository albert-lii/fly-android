package org.fly.router

import android.app.Application
import android.net.Uri
import com.alibaba.android.arouter.launcher.ARouter

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/20 8:43 下午
 * @description: ARouter工具类
 * @since: 1.0.0
 */
object Router {

    fun init(context: Application, debug: Boolean) {
        if (debug) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(context)
    }

    fun inject(what: Any) {
        ARouter.getInstance().inject(what)
    }

    fun path(path: String): PostcardWrapper {
        return PostcardWrapper(path)
    }

    fun uri(uri: Uri): PostcardWrapper {
        return PostcardWrapper(uri)
    }
}