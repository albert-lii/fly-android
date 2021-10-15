package org.fly.uikit.radio

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.Checkable
import androidx.appcompat.widget.AppCompatImageView
import org.fly.uikit.R


/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/9/8 2:04 下午
 * @description: Radio形式的ImageView
 * @since: 1.0.0
 */
open class RadioImageView : AppCompatImageView, Checkable {
    private val CHECK_STATE_SET = intArrayOf(android.R.attr.state_checked)

    private var isChecked = false
    var checkedDrawable: Drawable? = null
    var uncheckedDrawable: Drawable? = null
    private var checkedChangeListener: OnCheckedChangeListener? = null

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    @SuppressLint("CustomViewStyleable")
    private fun init(attrs: AttributeSet?) {
        attrs?.let {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.fly_uikit_RadioImageView)
            isChecked = ta.getBoolean(
                R.styleable.fly_uikit_RadioImageView_fu_checked,
                isChecked
            )
            checkedDrawable = ta.getDrawable(R.styleable.fly_uikit_RadioImageView_fu_checkSrc)
            uncheckedDrawable = ta.getDrawable(R.styleable.fly_uikit_RadioImageView_fu_uncheckSrc)
            ta.recycle()
        }
        setOnClickListener {
            toggle()
        }
    }

    /**
     * Imageview默认不支持state_checked状态，需要重写此方法
     */
    override fun onCreateDrawableState(extraSpace: Int): IntArray? {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECK_STATE_SET)
        }
        return drawableState
    }

    override fun setChecked(checked: Boolean) {
        this.isChecked = checked
        if (uncheckedDrawable == null) {
            refreshDrawableState()
        } else {
            setImageDrawable(if (checked) checkedDrawable else uncheckedDrawable)
        }
        checkedChangeListener?.onCheckedChanged(this, checked)
    }

    override fun isChecked(): Boolean {
        return isChecked
    }

    override fun toggle() {
        setChecked(!isChecked)
    }

    fun setOnCheckedChangeListener(listener: OnCheckedChangeListener?) {
        this.checkedChangeListener = listener
    }

    interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param view The compound button view whose state has changed.
         * @param isChecked  The new checked state of buttonView.
         */
        fun onCheckedChanged(view: RadioImageView?, isChecked: Boolean)
    }
}