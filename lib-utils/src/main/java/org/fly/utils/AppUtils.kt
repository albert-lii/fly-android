import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process

/**
 * App工具类
 */
@SuppressLint("StaticFieldLeak")
object AppUtils {
    private val initLock = Any()
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
    @SuppressLint("PrivateApi", "DiscouragedPrivateApi")
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

    /**
     * 检查App是否安装
     */
    @JvmStatic
    fun checkAppInstalled(packageName: String?): Boolean {
        val packageManager = getContext().packageManager
        var applicationInfo: ApplicationInfo? = null
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName!!, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (ep: Exception) {
            ep.printStackTrace()
        }
        return applicationInfo != null
    }

    /**
     * 判断当前进程是否是主进程
     */
    @JvmStatic
    fun isMainProcess(context: Context): Boolean {
        val processName = getProcessName(context)
        return context.packageName == processName
    }

    /**
     * 获取进程名
     */
    @JvmStatic
    fun getProcessName(context: Context): String {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningApps = am.runningAppProcesses ?: return ""
        for (proInfo in runningApps) {
            if (proInfo.pid == Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName
                }
            }
        }
        return ""
    }
}