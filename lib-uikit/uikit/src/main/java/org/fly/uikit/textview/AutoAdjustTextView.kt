package org.fly.uikit.textview

import android.content.Context
import android.text.TextPaint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * 自动适配文字尺寸的TextView
 * 当文字过大时，会自动缩小文字尺寸，直至合适为止；与原始的AppCompatTextView相比，原始的AppCompatTextView
 * 有autoSizeMinTextSize不能小于8dp的限制。一般情况下，AppCompatTextView已经足够使用
 *
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/9/6 4:54 下午
 * @since: 1.0.0
 */
open class AutoAdjustTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    var minTextSize: Float = 24f // 最小的文字大小
    var maxTextSize: Float = 48f // 最大的文字大小

    private var mPaint: TextPaint? = null

    private fun refitText(text: String, textWidth: Int) {
        if (textWidth <= 0 || text.isEmpty() || maxTextSize <= minTextSize) return
        if (mPaint == null) {
            mPaint = TextPaint(this.paint)
        }
        var fieldWidth =
            (textWidth - compoundPaddingLeft - compoundPaddingRight).coerceAtLeast(maxWidth)
        if (isPaddingOffsetRequired) {
            fieldWidth -= ((shadowRadius + shadowDx) * 2).toInt()
        }

        var dSize = maxTextSize
        while (dSize >= minTextSize) {
            mPaint!!.textSize = dSize
            val dW = mPaint!!.measureText(text)
            if (dW <= fieldWidth) {
                break
            }
            dSize = dSize.minus(1f)
        }
        textSize = dSize / resources.displayMetrics.scaledDensity
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w != oldw) {
            refitText(text.toString(), w)
        }
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        refitText(text?.toString() ?: "", width)
    }
}