package org.we.fly.widget.eilstextview

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/26 11:32 PM
 * @description: 去除自带行间距的TextView
 * @since: 1.0.0
 */
class ExInnerLineSpaceTextView : AppCompatTextView {
    constructor(context: Context?) : super(context!!) {

    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {

    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        defStyleAttr
    ) {

    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(getEilsText(text), type)
    }

    /**
     * 去除每行文字间的自带行间距
     *
     * @param text
     */
    private fun getEilsText(text: CharSequence?): SpannableStringBuilder? {
        if (text == null) {
            return null
        }
        // 获得视觉定义的每行文字的行高
        val lineHeight = textSize.toInt()
        val ssb: SpannableStringBuilder
        if (text is SpannableStringBuilder) {
            ssb = text
            // 设置LineHeightSpan
            ssb.setSpan(
                ExInnerLineSpaceSpan(lineHeight),
                0,
                text.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } else {
            ssb = SpannableStringBuilder(text)
            // 设置LineHeightSpan
            ssb.setSpan(
                ExInnerLineSpaceSpan(lineHeight),
                0,
                text.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return ssb
    }
}