package org.fly.scan

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView


/**
 * 扫描动画控件
 *
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/5 1:48 下午
 * @since: 1.0.0
 */
class ScanFinderView : FrameLayout {
    private var duration = 4000 // 扫描动画执行时间
    private lateinit var lineView: ImageView
    private var animator: ObjectAnimator? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    @SuppressLint("CustomViewStyleable")
    fun init(context: Context, attrs: AttributeSet?) {
        lineView = ImageView(context)
        lineView.adjustViewBounds = true
        val lineViewLp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        addView(lineView, lineViewLp)

        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.fly_ScanFinderView)
            val lineDrawable = ta.getDrawable(R.styleable.fly_ScanFinderView_scv_line)
            lineDrawable?.let {
                lineView.setImageDrawable(lineDrawable)
            }
            duration = ta.getInteger(R.styleable.fly_ScanFinderView_scv_duration, duration)
            ta.recycle()
        }

        lineView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                lineView.viewTreeObserver.removeOnGlobalLayoutListener(this);
                startScanAnim()
            }
        })
    }

    /**
     * 开始扫描动画
     */
    fun startScanAnim() {
        if (animator?.isRunning ?: false) {
            stopScanAnim()
        }
        val lineHeight = lineView.height
        animator = ObjectAnimator.ofFloat(
            lineView,
            "translationY",
            0f - lineHeight,
            (measuredHeight - paddingTop - paddingBottom - lineHeight).toFloat()
        )
        animator?.let {
            it.repeatMode = ValueAnimator.RESTART
            it.repeatCount = ValueAnimator.INFINITE
            it.interpolator = LinearInterpolator()
            it.duration = duration.toLong()
        }
        animator?.start()
    }

    /**
     * 停止扫描动画
     */
    fun stopScanAnim() {
        animator?.cancel()
        animator = null
    }
}