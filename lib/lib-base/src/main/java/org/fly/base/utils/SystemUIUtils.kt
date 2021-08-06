package org.fly.base.utils

import android.Manifest.permission
import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Point
import android.os.Build
import android.view.*
import android.view.ViewGroup.MarginLayoutParams
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/7/23 10:11 上午
 * @description: -
 * @since: 1.0.0
 */
object SystemUIUtils {

    /**=============================================================================================
     * status bar
     **===========================================================================================*/

    private const val TAG_STATUS_BAR = "TAG_STATUS_BAR"
    private const val TAG_OFFSET = "TAG_OFFSET"
    private const val KEY_OFFSET = -123

    /**
     * 获取StatusBar的高度
     */
    @JvmStatic
    fun getStatusBarHeight(): Int {
        val resources = Resources.getSystem()
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }

    /**
     * 设置StatusBar是否可见
     */
    @JvmStatic
    fun setStatusBarVisibility(
        activity: Activity,
        isVisible: Boolean
    ) {
        setStatusBarVisibility(activity.window, isVisible)
    }

    /**
     * 设置StatusBar是否可见
     */
    @JvmStatic
    fun setStatusBarVisibility(
        window: Window,
        isVisible: Boolean
    ) {
        if (isVisible) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            showStatusBarView(window)
            addMarginTopEqualStatusBarHeight(window)
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            hideStatusBarView(window)
            subtractMarginTopEqualStatusBarHeight(window)
        }
    }

    /**
     * StatusBar是否可见
     */
    @JvmStatic
    fun isStatusBarVisible(activity: Activity): Boolean {
        val flags = activity.window.attributes.flags
        return flags and WindowManager.LayoutParams.FLAG_FULLSCREEN == 0
    }

    /**
     * 将StatusBar变透明
     */
    @JvmStatic
    fun transparentStatusBar(activity: Activity) {
        transparentStatusBar(activity.window)
    }

    /**
     * 将StatusBar变透明
     */
    @JvmStatic
    fun transparentStatusBar(window: Window) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            val option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            val vis = window.decorView.systemUiVisibility
            window.decorView.systemUiVisibility = option or vis
            window.statusBarColor = Color.TRANSPARENT
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    /**
     * 为View添加MarginTop，MarginTop距离等于StatusBar的高度
     */
    @JvmStatic
    fun addMarginTopEqualStatusBarHeight(view: View) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
        view.tag = TAG_OFFSET
        val haveSetOffset = view.getTag(KEY_OFFSET)
        if (haveSetOffset != null && haveSetOffset as Boolean) return
        val layoutParams = view.layoutParams as MarginLayoutParams
        layoutParams.setMargins(
            layoutParams.leftMargin,
            layoutParams.topMargin + getStatusBarHeight(),
            layoutParams.rightMargin,
            layoutParams.bottomMargin
        )
        view.setTag(KEY_OFFSET, true)
    }

    /**
     * 为View减去MarginTop，MarginTop距离等于StatusBar的高度
     */
    @JvmStatic
    fun subtractMarginTopEqualStatusBarHeight(view: View) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
        val haveSetOffset = view.getTag(KEY_OFFSET)
        if (haveSetOffset == null || !(haveSetOffset as Boolean)) return
        val layoutParams = view.layoutParams as MarginLayoutParams
        layoutParams.setMargins(
            layoutParams.leftMargin,
            layoutParams.topMargin - getStatusBarHeight(),
            layoutParams.rightMargin,
            layoutParams.bottomMargin
        )
        view.setTag(KEY_OFFSET, false)
    }

    private fun addMarginTopEqualStatusBarHeight(window: Window) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
        val withTag = window.decorView.findViewWithTag<View>(TAG_OFFSET) ?: return
        addMarginTopEqualStatusBarHeight(withTag)
    }

    private fun subtractMarginTopEqualStatusBarHeight(window: Window) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
        val withTag = window.decorView.findViewWithTag<View>(TAG_OFFSET) ?: return
        subtractMarginTopEqualStatusBarHeight(withTag)
    }

    /**
     * 设置StatusBar的颜色
     */
    @JvmStatic
    fun setStatusBarColor(
        activity: Activity,
        @ColorInt color: Int
    ): View? {
        return setStatusBarColor(activity, color, false)
    }

    /**
     * 设置StatusBar的颜色
     *
     * @param activity The activity.
     * @param color    The status bar's color.
     * @param isDecor  True to add fake status bar in DecorView
     *                 False to add fake status bar in ContentView.
     */
    @JvmStatic
    fun setStatusBarColor(
        activity: Activity,
        @ColorInt color: Int,
        isDecor: Boolean
    ): View? {
        return setStatusBarColor(activity.window, color, isDecor)
    }

    /**
     * 设置StatusBar的颜色
     */
    @JvmStatic
    fun setStatusBarColor(
        window: Window,
        @ColorInt color: Int
    ): View? {
        return setStatusBarColor(window, color, false)
    }

    /**
     * 设置StatusBar的颜色
     *
     * @param window  The window.
     * @param color   The status bar's color.
     * @param isDecor True to add fake status bar in DecorView,
     *                False to add fake status bar in ContentView.
     */
    @JvmStatic
    fun setStatusBarColor(
        window: Window,
        @ColorInt color: Int,
        isDecor: Boolean
    ): View? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return null
        transparentStatusBar(window)
        return applyStatusBarColor(window, color, isDecor)
    }

    private fun applyStatusBarColor(
        activity: Activity,
        color: Int,
        isDecor: Boolean
    ): View? {
        return applyStatusBarColor(activity.window, color, isDecor)
    }

    private fun applyStatusBarColor(
        window: Window,
        color: Int,
        isDecor: Boolean
    ): View? {
        val parent =
            if (isDecor) window.decorView as ViewGroup else (window.findViewById<View>(R.id.content) as ViewGroup)
        var fakeStatusBarView = parent.findViewWithTag<View>(TAG_STATUS_BAR)
        if (fakeStatusBarView != null) {
            if (fakeStatusBarView.visibility == View.GONE) {
                fakeStatusBarView.visibility = View.VISIBLE
            }
            fakeStatusBarView.setBackgroundColor(color)
        } else {
            fakeStatusBarView = createStatusBarView(window.context, color)
            parent.addView(fakeStatusBarView)
        }
        return fakeStatusBarView
    }

    private fun hideStatusBarView(activity: Activity) {
        hideStatusBarView(activity.window)
    }

    private fun hideStatusBarView(window: Window) {
        val decorView = window.decorView as ViewGroup
        val fakeStatusBarView = decorView.findViewWithTag<View>(TAG_STATUS_BAR) ?: return
        fakeStatusBarView.visibility = View.GONE
    }

    private fun showStatusBarView(window: Window) {
        val decorView = window.decorView as ViewGroup
        val fakeStatusBarView = decorView.findViewWithTag<View>(TAG_STATUS_BAR) ?: return
        fakeStatusBarView.visibility = View.VISIBLE
    }

    private fun createStatusBarView(
        context: Context,
        color: Int
    ): View? {
        val statusBarView = View(context)
        statusBarView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight()
        )
        statusBarView.setBackgroundColor(color)
        statusBarView.tag = TAG_STATUS_BAR
        return statusBarView
    }


    /**=============================================================================================
     * notification bar
     **===========================================================================================*/

    /**
     * 设置通知栏是否可见
     */
    @RequiresPermission(permission.EXPAND_STATUS_BAR)
    @JvmStatic
    fun setNotificationBarVisibility(isVisible: Boolean) {
        val methodName: String
        methodName = if (isVisible) {
            if (Build.VERSION.SDK_INT <= 16) "expand" else "expandNotificationsPanel"
        } else {
            if (Build.VERSION.SDK_INT <= 16) "collapse" else "collapsePanels"
        }
        invokePanels(methodName)
    }

    @SuppressLint("WrongConstant")
    private fun invokePanels(methodName: String) {
        try {
            val service =
                AppUtils.getApplication().getSystemService("statusbar")
            @SuppressLint("PrivateApi") val statusBarManager =
                Class.forName("android.app.StatusBarManager")
            val expand = statusBarManager.getMethod(methodName)
            expand.invoke(service)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**=============================================================================================
     * nav bar
     **===========================================================================================*/
    /**
     * 获取NavBar的高度
     *
     * @return the navigation bar's height
     */
    @JvmStatic
    fun getNavBarHeight(): Int {
        val res = Resources.getSystem()
        val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId != 0) {
            res.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    /**
     * 设置NavBar是否可见
     */
    @JvmStatic
    fun setNavBarVisibility(activity: Activity, isVisible: Boolean) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
        setNavBarVisibility(activity.window, isVisible)
    }

    /**
     * 设置NavBar是否可见
     */
    @JvmStatic
    fun setNavBarVisibility(window: Window, isVisible: Boolean) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
        val decorView = window.decorView as ViewGroup
        var i = 0
        val count = decorView.childCount
        while (i < count) {
            val child = decorView.getChildAt(i)
            val id = child.id
            if (id != View.NO_ID) {
                val resourceEntryName = getResNameById(id)
                if ("navigationBarBackground" == resourceEntryName) {
                    child.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
                }
            }
            i++
        }
        val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        if (isVisible) {
            decorView.systemUiVisibility = decorView.systemUiVisibility and uiOptions.inv()
        } else {
            decorView.systemUiVisibility = decorView.systemUiVisibility or uiOptions
        }
    }

    /**
     * 判断NavBar是否可见
     */
    @JvmStatic
    fun isNavBarVisible(activity: Activity): Boolean {
        return isNavBarVisible(activity.window)
    }

    /**
     * 判断NavBar是否可见
     */
    @JvmStatic
    fun isNavBarVisible(window: Window): Boolean {
        var isVisible = false
        val decorView = window.decorView as ViewGroup
        var i = 0
        val count = decorView.childCount
        while (i < count) {
            val child = decorView.getChildAt(i)
            val id = child.id
            if (id != View.NO_ID) {
                val resourceEntryName = getResNameById(id)
                if ("navigationBarBackground" == resourceEntryName && child.visibility == View.VISIBLE) {
                    isVisible = true
                    break
                }
            }
            i++
        }
        if (isVisible) {
            val visibility = decorView.systemUiVisibility
            isVisible = visibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION == 0
        }
        return isVisible
    }

    private fun getResNameById(id: Int): String {
        return try {
            AppUtils.getContext().resources.getResourceEntryName(id)
        } catch (ignore: Exception) {
            ""
        }
    }

    /**
     * 设置NavBar的颜色
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @JvmStatic
    fun setNavBarColor(activity: Activity, @ColorInt color: Int) {
        setNavBarColor(activity.window, color)
    }

    /**
     * 设置NavBar的颜色
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @JvmStatic
    fun setNavBarColor(window: Window, @ColorInt color: Int) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.navigationBarColor = color
    }

    /**
     * 获取NavBar的颜色
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @JvmStatic
    fun getNavBarColor(activity: Activity): Int {
        return getNavBarColor(activity.window)
    }

    /**
     * 获取NavBar的颜色
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @JvmStatic
    fun getNavBarColor(window: Window): Int {
        return window.navigationBarColor
    }

    /**
     * 是否有NavBar
     */
    @JvmStatic
    fun isSupportNavBar(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val wm = AppUtils.getApplication()
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            val realSize = Point()
            display.getSize(size)
            display.getRealSize(realSize)
            return realSize.y != size.y || realSize.x != size.x
        }
        val menu = ViewConfiguration.get(AppUtils.getContext()).hasPermanentMenuKey()
        val back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
        return !menu && !back
    }
}