package org.fly.uikit.round

import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.view.View
import org.fly.uikit.R

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/30 7:57 下午
 * @description: 圆角工具类，基于重新设置GradientDrawable实现；
 *               不使用canvas.clipPath()，因为在数据量大时，此方法比较消耗资源，容易卡顿；
 *               关于ImageView的圆角状态建议直接使用Glide或者其他图片加载工具实现
 * @since: 1.0.0
 */
class RHelper(private val view: View) : IRound {

    companion object {
        // 无效的颜色
        const val INVALID_COLOR = -1
        const val NO_CORNER = 0f
    }

    private var topLeftRadius: Float = 0f
    private var topRightRadius: Float = 0f
    private var bottomLeftRadius: Float = 0f
    private var bottomRightRadius: Float = 0f
    private var normalColor: Int = Color.GRAY
    private var disabledColor: Int = Color.DKGRAY
    private var pressedColor: Int = INVALID_COLOR
    private var borderWidth: Int = 0
    private var borderNormalColor: Int = Color.TRANSPARENT
    private var borderDisabledColor: Int = Color.TRANSPARENT
    private var borderPressedColor: Int = Color.TRANSPARENT

    fun initAttrs(ta: TypedArray) {
        topLeftRadius = ta.getDimension(
            R.styleable.fly_uikit_RoundView_frv_topLeftRadius,
            NO_CORNER
        )

        topRightRadius = ta.getDimension(
            R.styleable.fly_uikit_RoundView_frv_topRightRadius,
            NO_CORNER
        )

        bottomLeftRadius = ta.getDimension(
            R.styleable.fly_uikit_RoundView_frv_bottomLeftRadius,
            NO_CORNER
        )

        bottomRightRadius = ta.getDimension(
            R.styleable.fly_uikit_RoundView_frv_bottomRightRadius,
            NO_CORNER
        )

        val radius = ta.getDimension(
            R.styleable.fly_uikit_RoundView_frv_radius,
            NO_CORNER
        )
        setRadius(radius)

        normalColor = ta.getColor(
            R.styleable.fly_uikit_RoundView_frv_normalColor,
            Color.GRAY
        )

        disabledColor = ta.getColor(
            R.styleable.fly_uikit_RoundView_frv_disabledColor,
            INVALID_COLOR
        )

        pressedColor = ta.getColor(
            R.styleable.fly_uikit_RoundView_frv_pressedColor,
            INVALID_COLOR
        )

        borderWidth = ta.getDimension(
            R.styleable.fly_uikit_RoundView_frv_borderWidth,
            0f
        ).toInt()

        borderNormalColor = ta.getColor(
            R.styleable.fly_uikit_RoundView_frv_borderNormalColor,
            Color.TRANSPARENT
        )

        borderPressedColor = ta.getColor(
            R.styleable.fly_uikit_RoundView_frv_borderPressedColor,
            Color.TRANSPARENT
        )

        borderDisabledColor = ta.getColor(
            R.styleable.fly_uikit_RoundView_frv_borderDisabledColor,
            Color.TRANSPARENT
        )
    }


    override fun setTopLeftRadius(radius: Float) {
        this.topLeftRadius = radius
    }

    override fun setTopRightRadius(radius: Float) {
        this.topRightRadius = radius
    }

    override fun setBottomLeftRadius(radius: Float) {
        this.bottomLeftRadius = radius
    }

    override fun setBottomRightRadius(radius: Float) {
        this.bottomRightRadius = radius
    }

    override fun setRadius(radius: Float) {
        this.topLeftRadius = radius
        this.topRightRadius = radius
        this.bottomLeftRadius = radius
        this.bottomRightRadius = radius
    }

    override fun setNormalColor(color: Int) {
        this.normalColor = color
    }

    override fun setDisabledColor(color: Int) {
        this.disabledColor = color
    }

    override fun setPressedColor(color: Int) {
        this.pressedColor = color
    }

    override fun setBorderWidth(size: Int) {
        this.borderWidth = size
    }

    override fun setNormalBorderColor(color: Int) {
        this.borderNormalColor = color
    }

    override fun setDisabledBorderColor(color: Int) {
        this.borderDisabledColor = color
    }

    override fun setPressedBorderColor(color: Int) {
        this.borderPressedColor = color
    }

    /**
     * 处理圆角背景
     */
    override fun processRoundBackground() {
        val radiusArray = FloatArray(8)
        radiusArray[0] = topLeftRadius
        radiusArray[1] = topLeftRadius
        radiusArray[2] = topRightRadius
        radiusArray[3] = topRightRadius
        radiusArray[4] = bottomRightRadius
        radiusArray[5] = bottomRightRadius
        radiusArray[6] = bottomLeftRadius
        radiusArray[7] = bottomLeftRadius
        val stateListDrawable = StateListDrawable()
        if (pressedColor != INVALID_COLOR || borderWidth > 0) {
            val pressedDrawable = GradientDrawable()
            pressedDrawable.shape = GradientDrawable.RECTANGLE
            pressedDrawable.cornerRadii = radiusArray
            if (pressedColor != INVALID_COLOR) {
                pressedDrawable.setColor(pressedColor)
            }
            if (borderWidth > 0) {
                pressedDrawable.setStroke(borderWidth, borderPressedColor)
            }
            stateListDrawable.addState(intArrayOf(android.R.attr.state_pressed), pressedDrawable)
        }
        if (disabledColor != INVALID_COLOR || borderWidth > 0) {
            val disabledDrawable = GradientDrawable()
            disabledDrawable.shape = GradientDrawable.RECTANGLE
            disabledDrawable.cornerRadii = radiusArray
            if (pressedColor != INVALID_COLOR) {
                disabledDrawable.setColor(pressedColor)
            }
            if (borderWidth > 0) {
                disabledDrawable.setStroke(borderWidth, borderDisabledColor)
            }
            disabledDrawable.setStroke(borderWidth, borderDisabledColor)
            stateListDrawable.addState(intArrayOf(-android.R.attr.state_enabled), disabledDrawable)
        }
        val normalDrawable = GradientDrawable()
        normalDrawable.shape = GradientDrawable.RECTANGLE
        normalDrawable.cornerRadii = radiusArray
        normalDrawable.setColor(normalColor)
        if (borderWidth > 0) {
            normalDrawable.setStroke(borderWidth, borderNormalColor)
        }
        stateListDrawable.addState(intArrayOf(), normalDrawable)
        view.background = stateListDrawable
    }
}
