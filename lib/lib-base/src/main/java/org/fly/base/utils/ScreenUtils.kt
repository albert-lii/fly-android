package org.fly.base.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.os.Build
import android.view.View
import android.view.WindowManager
import com.blankj.utilcode.util.Utils

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/7/21 3:41 下午
 * @description: 屏幕相关工具类
 * @since: 1.0.0
 */
object ScreenUtils {

    fun getDensity(context: Context): Float {
        return context.resources.displayMetrics.density
    }

    /**
     * 获取屏幕宽度
     */
    @JvmStatic
    fun getScreenWidth(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager ?: return -1
        val point = Point()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.defaultDisplay.getRealSize(point)
        } else {
            wm.defaultDisplay.getSize(point)
        }
        return point.x
    }

    /**
     * 获取屏幕高度
     */
    @JvmStatic
    fun getScreenHeight(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager ?: return -1
        val point = Point()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.defaultDisplay.getRealSize(point)
        } else {
            wm.defaultDisplay.getSize(point)
        }
        return point.y
    }

    /**
     * 获取应用的屏幕宽度
     */
    @JvmStatic
    fun getAppScreenWidth(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager ?: return -1
        val point = Point()
        wm.defaultDisplay.getSize(point)
        return point.x
    }

    /**
     * 获取应用的屏幕高度
     */
    @JvmStatic
    fun getAppScreenHeight(): Int {
        val wm = Utils.getApp().getSystemService(Context.WINDOW_SERVICE) as WindowManager
            ?: return -1
        val point = Point()
        wm.defaultDisplay.getSize(point)
        return point.y
    }


    /**
     * 获取状态栏高度
     */
    @JvmStatic
    fun getStatuesBarHeight(): Int {
        val res = Resources.getSystem()
        val resourceId = res.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId != 0) res.getDimensionPixelSize(resourceId) else 0
    }

    /**
     * 获取底部导航度
     */
    @JvmStatic
    fun getNavBarHeight(): Int {
        val res = Resources.getSystem()
        val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId != 0) res.getDimensionPixelSize(resourceId) else 0
    }
}