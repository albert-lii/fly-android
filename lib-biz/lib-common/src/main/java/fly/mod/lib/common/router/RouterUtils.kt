package fly.mod.lib.common.router

import android.content.Context
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter


/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/3/15 5:16 PM
 * @description: 路由工具类
 * @since: 1.0.0
 */
class RouterUtils {
    companion object {

        @JvmStatic
        fun getInstance(): RouterUtils {
            return SingletonHolder.holder
        }
    }

    private object SingletonHolder {
        val holder = RouterUtils()
    }

    fun build(path: String): PostcardWrapper {
        return PostcardWrapper(path)
    }

    fun builds(path: String): Postcard {
        return ARouter.getInstance().build(path)
    }

    /**
     * 简单跳转
     */
    fun navigate(context: Context, path: String): Any? {
        return build(path).navigate(context)
    }

    /**
     * singeTop模式的跳转
     */
    fun navigateBySingleTop(context: Context, path: String): Any? {
        return build(path)
            .singleTop()
            .navigate(context)
    }

    /**
     * singeTask模式的跳转
     */
    fun navigateBySingleTask(context: Context, path: String): Any? {
        return build(path)
            .singleTask()
            .navigate(context)
    }
}