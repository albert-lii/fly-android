package org.we.fly.widget.eilstextview

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import org.we.fly.R

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/26 11:32 PM
 * @description: 去除自带行间距的TextView
 * @since: 1.0.0
 */
class EilsTextView : AppCompatTextView {
    constructor(context: Context?) : super(context!!) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.EilsTextView)
            val text = ta.getString(R.styleable.EilsTextView_et_eilsText)
            setEilsText(text)
            ta.recycle()
        }
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
    }

    /**
     * 去除每行文字间的自带行间距
     *
     * @param text
     */
    fun setEilsText(text: CharSequence?) {
        if (text == null) {
            return
        }
        // 获得视觉定义的每行文字的行高
        val lineHeight = textSize.toInt()
        val ssb: SpannableStringBuilder
        if (text is SpannableStringBuilder) {
            ssb = text
            // 设置LineHeightSpan
            ssb.setSpan(
                ExcludeInnerLineSpacepan(lineHeight),
                0,
                text.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } else {
            ssb = SpannableStringBuilder(text)
            // 设置LineHeightSpan
            ssb.setSpan(
                ExcludeInnerLineSpacepan(lineHeight),
                0,
                text.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        // 调用系统setText()方法
        setText(ssb)
    }
}