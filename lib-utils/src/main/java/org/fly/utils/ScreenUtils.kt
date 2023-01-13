package org.fly.utils

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.LruCache
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.ScrollView
import androidx.core.graphics.createBitmap
import androidx.recyclerview.widget.RecyclerView

/**
 * 屏幕相关工具类
 */
object ScreenUtils {

    /**
     * 获取状态栏高度
     */
    @JvmStatic
    fun getStatusBarHeight(): Int {
        val resources = Resources.getSystem()
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }

    /**
     * 获取底部导航栏高度
     */
    @JvmStatic
    fun getNavBarHeight(): Int {
        val resources = Resources.getSystem()
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId != 0) {
            resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    /**
     * 获取屏幕宽度
     */
    @JvmStatic
    fun getScreenWidth(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager? ?: return -1
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
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager? ?: return -1
        val point = Point()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.defaultDisplay.getRealSize(point)
        } else {
            wm.defaultDisplay.getSize(point)
        }
        return point.y
    }

    /**
     * 设置横屏
     */
    @JvmStatic
    fun setLandscape(activity: Activity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    /**
     * 设置竖屏
     */
    @JvmStatic
    fun setPortrait(activity: Activity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    /**
     * 截屏，除去导航栏
     */
    @JvmStatic
    fun captureScreen(activity: Activity, bitmapConfig: Bitmap.Config = Bitmap.Config.RGB_565) {
        captureView(activity.window.decorView, bitmapConfig)
    }

    /**
     * 截取View的图片
     *
     * View已经在界面上展示了，可以直接获取View的缓存
     * 对View进行量测，布局后生成View的缓存
     * View为固定大小的View，包括TextView,ImageView,LinearLayout,FrameLayout,RelativeLayout等
     * @param view 截取的View,View必须有固定的大小，不然drawingCache返回null
     * @param bitmapConfig 如果是临时性的截图，可以选择565这种较低的配置减少消耗
     * @return 生成的Bitmap
     */
    @JvmStatic
    fun captureView(view: View, bitmapConfig: Bitmap.Config = Bitmap.Config.ARGB_8888): Bitmap? {
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        // 重新测量一遍View的宽高
        view.measure(
            View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(view.height, View.MeasureSpec.EXACTLY)
        )
        // 确定View的位置
        view.layout(
            view.x.toInt(), view.y.toInt(), view.x.toInt() + view.measuredWidth,
            view.y.toInt() + view.measuredHeight
        )
        // 生成View宽高一样的Bitmap
        var bitmap: Bitmap? = null
        if (view.drawingCache != null && view.measuredWidth > 0 && view.measuredHeight > 0) {
            bitmap = Bitmap.createBitmap(
                view.drawingCache, 0, 0, view.measuredWidth,
                view.measuredHeight
            )
        }
        view.isDrawingCacheEnabled = false
        view.destroyDrawingCache()
        // 有出现view.drawingCache为空的情况,兜底处理
        if (bitmap == null && view.measuredWidth > 0 && view.measuredHeight > 0) {
            bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, bitmapConfig)
            val c = Canvas(bitmap!!)
            view.draw(c)
        }
        return bitmap
    }

    /**
     * 截取ScrollerView
     * 原理：获取scrollView的子View的高度，然后创建一个子View宽高的画布，将ScrollView绘制在画布上
     */
    fun captureScrollView(scrollView: ScrollView): Bitmap {
        var h = 0
        for (i in 0 until scrollView.childCount) {
            val childView = scrollView.getChildAt(i)
            // 获取子View的高度
            h += childView.height
            // 设置背景颜色，避免布局里未设置背景颜色，截的图背景黑色
            childView.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }

        val bitmap = createBitmap(scrollView.width, h)
        val canvas = Canvas(bitmap)
        // 将ScrollView绘制在画布上
        scrollView.draw(canvas)
        return bitmap
    }

    /**
     * 截取LinearLayout图像
     * 原理：获取scrollView的子View的高度，然后创建一个子View宽高的画布，将LinearLayout绘制在画布上
     */
    fun captureLinearLayout(scrollView: LinearLayout): Bitmap {
        var h = 0
        for (i in 0 until scrollView.childCount) {
            val childView = scrollView.getChildAt(i)
            // 获取子View的高度
            h += childView.height
            // 设置背景颜色，避免布局里未设置背景颜色，截的图背景黑色
            childView.setBackgroundColor(Color.RED)
        }

        val bitmap = createBitmap(scrollView.width, h)
        val canvas = Canvas(bitmap)
        // 将ScrollView绘制在画布上
        scrollView.draw(canvas)
        return bitmap
    }

    /**
     * 截取ListView图像
     * 原理：获取到每一个子View，将子View生成的bitmap存入集合，并且累积ListView高度
     * 遍历完成后，创建一个ListView大小的画布，将集合的Bitmap绘制到画布上
     */
    fun captureListView(listView: ListView): Bitmap? {
        val adapter = listView.adapter
        val itemCount = adapter.count
        var allitemsheight = 0
        val bitmaps = ArrayList<Bitmap>()

        for (i in 0 until itemCount) {
            // 获取每一个子View
            val childView = adapter.getView(i, null, listView)
            // 测量宽高
            childView.measure(
                View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))

            // 布局位置
            childView.layout(0, 0, childView.measuredWidth, childView.measuredHeight)
            // 设置背景颜色，避免是黑色的
            childView.setBackgroundColor(Color.parseColor("#FFFFFF"))
            childView.isDrawingCacheEnabled = true
            // 生成缓存
            childView.buildDrawingCache()
            // 将每一个View的截图加入集合
            bitmaps.add(childView.drawingCache)
            // 叠加截图高度
            allitemsheight += childView.measuredHeight
        }

        // 创建和ListView宽高一样的画布
        val bitmap = createBitmap(listView.measuredWidth, allitemsheight)
        val canvas = Canvas(bitmap)

        val paint = Paint()
        var iHeight = 0f

        for (i in bitmaps.indices) {
            val bmp: Bitmap = bitmaps[i]
            // 将每一个生成的bitmap绘制在画布上
            canvas.drawBitmap(bmp, 0f, iHeight, paint)
            iHeight += bmp.height

            bmp.recycle()
        }
        return bitmap
    }

    /**
     * 截取RecyclerView图像
     * 原理和ListView集合是一样的，获取到每一个Holder的截图放入集合，最后统一绘制到Bitmap上
     */
    fun captureRecyclerView(recyclerView: RecyclerView): Bitmap? {
        val adapter = recyclerView.adapter
        var bigBitmap: Bitmap? = null
        if (adapter != null) {
            val size = adapter.itemCount
            var height = 0
            val paint = Paint()
            var iHeight = 0
            val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

            // Use 1/8th of the available memory for this memory cache.
            val cacheSize = maxMemory / 8
            val bitmapCache = LruCache<String, Bitmap>(cacheSize)
            for (i in 0 until size) {
                val holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i))
                adapter.onBindViewHolder(holder, i)
                holder.itemView.measure(
                    View.MeasureSpec.makeMeasureSpec(recyclerView.width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                holder.itemView.layout(0, 0, holder.itemView.measuredWidth,
                    holder.itemView.measuredHeight)
                holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
                holder.itemView.isDrawingCacheEnabled = true
                holder.itemView.buildDrawingCache()
                val drawingCache = holder.itemView.drawingCache
                if (drawingCache != null) {
                    bitmapCache.put(i.toString(), drawingCache)
                }
                height += holder.itemView.measuredHeight
            }

            bigBitmap = createBitmap(recyclerView.measuredWidth, height)
            val bigCanvas = Canvas(bigBitmap!!)
            val lBackground = recyclerView.background
            if (lBackground is ColorDrawable) {
                val lColor = lBackground.color
                bigCanvas.drawColor(lColor)
            }

            for (i in 0 until size) {
                val bitmap = bitmapCache.get(i.toString())
                bigCanvas.drawBitmap(bitmap, 0f, iHeight.toFloat(), paint)
                iHeight += bitmap.height
                bitmap.recycle()
            }
        }
        return bigBitmap
    }
}