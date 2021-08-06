package org.fly.base.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/7/20 5:08 下午
 * @description: App工具类
 * @since: 1.0.0
 */
@SuppressLint("StaticFieldLeak")
object AppUtils {
    private val initLock = Any()
    private var isDebug: Boolean = true // 应用是否属于调试模式
    private var context: Context? = null
    private var application: Application? = null

    @JvmStatic
    fun init(application: Application) {
        synchronized(initLock) {
            context = application.applicationContext
            this.application = application
        }
    }

    @JvmStatic
    fun setDebug(isDebug: Boolean) {
        this.isDebug = isDebug
    }

    @JvmStatic
    fun isDebug(): Boolean = isDebug

    @JvmStatic
    fun getContext(): Context {
        if (context == null) {
            context = getApplication().applicationContext
        }
        return context!!
    }


    @JvmStatic
    fun getApplication(): Application {
        if (application == null) {
            synchronized(initLock) {
                if (application == null) {
                    application = getCurrentApplicationByReflect()
                }
            }
        }
        return application!!
    }

    /**
     * 判断Activity是否销毁
     *
     * @param activity
     * @return true or false
     */
    @JvmStatic
    fun isActivityDestroy(activity: Activity?): Boolean {
        if (activity == null) {
            return true
        }
        return activity.isFinishing || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed)
    }

    /**
     * 通过反射获取当前应用application实例
     */
    private fun getCurrentApplicationByReflect(): Application? {
        try {
            var application = Class.forName("android.app.ActivityThread")
                ?.getDeclaredMethod("currentApplication")?.invoke(null) as? Application
            if (application != null) {
                return application
            }

            application = Class.forName("android.app.AppGlobals")
                ?.getDeclaredMethod("getInitialApplication")?.invoke(null) as? Application
            if (application != null) {
                return application
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 获取VersionCode
     */
    @JvmStatic
    fun getVersionCode(): Long {
        var result = 0L
        val context = getContext()
        try {
            val packageInfo = getContext().packageManager.getPackageInfo(context.packageName, 0)
            result = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                packageInfo.versionCode.toLong()
            } else {
                packageInfo.longVersionCode
            }
        } catch (throwable: Throwable) {
        }
        return result
    }

    /**
     * 获取VersionName
     */
    @JvmStatic
    fun getVersionName(): String {
        var result = ""
        val context = getContext()
        try {
            result = context.packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (throwable: Throwable) {
        }
        return result
    }
}