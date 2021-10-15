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
    private var normalBgColor: Int = Color.TRANSPARENT
    private var normalBorderColor: Int = Color.TRANSPARENT
    private var disabledBgColor: Int = Color.DKGRAY
    private var disabledBorderColor: Int = Color.TRANSPARENT
    private var pressedBgColor: Int = INVALID_COLOR
    private var pressedBorderColor: Int = Color.TRANSPARENT
    private var borderWidth: Int = 0


    fun initAttrs(ta: TypedArray) {
        topLeftRadius = ta.getDimension(
            R.styleable.fly_uikit_RoundView_fu_topLeftRadius,
            NO_CORNER
        )

        topRightRadius = ta.getDimension(
            R.styleable.fly_uikit_RoundView_fu_topRightRadius,
            NO_CORNER
        )

        bottomLeftRadius = ta.getDimension(
            R.styleable.fly_uikit_RoundView_fu_bottomLeftRadius,
            NO_CORNER
        )

        bottomRightRadius = ta.getDimension(
            R.styleable.fly_uikit_RoundView_fu_bottomRightRadius,
            NO_CORNER
        )

        val radius = ta.getDimension(
            R.styleable.fly_uikit_RoundView_fu_radius,
            -1f
        )
        if (radius != -1f) {
            setRadius(radius)
        }

        normalBgColor = ta.getColor(
            R.styleable.fly_uikit_RoundView_fu_normalBgColor,
            Color.GRAY
        )

        disabledBgColor = ta.getColor(
            R.styleable.fly_uikit_RoundView_fu_disabledBgColor,
            INVALID_COLOR
        )

        pressedBgColor = ta.getColor(
            R.styleable.fly_uikit_RoundView_fu_pressedBgColor,
            INVALID_COLOR
        )

        borderWidth = ta.getDimension(
            R.styleable.fly_uikit_RoundView_fu_borderWidth,
            0f
        ).toInt()

        normalBorderColor = ta.getColor(
            R.styleable.fly_uikit_RoundView_fu_normalBorderColor,
            Color.TRANSPARENT
        )

        pressedBorderColor = ta.getColor(
            R.styleable.fly_uikit_RoundView_fu_pressedBorderColor,
            Color.TRANSPARENT
        )

        disabledBorderColor = ta.getColor(
            R.styleable.fly_uikit_RoundView_fu_disabledBorderColor,
            Color.TRANSPARENT
        )
    }

    override fun setTopLeftRadius(radius: Float) {
        this.topLeftRadius = radius
        buildRoundBackground()
    }

    override fun setTopRightRadius(radius: Float) {
        this.topRightRadius = radius
        buildRoundBackground()
    }

    override fun setBottomLeftRadius(radius: Float) {
        this.bottomLeftRadius = radius
        buildRoundBackground()
    }

    override fun setBottomRightRadius(radius: Float) {
        this.bottomRightRadius = radius
        buildRoundBackground()
    }

    override fun setRadius(radius: Float) {
        this.topLeftRadius = radius
        this.topRightRadius = radius
        this.bottomRightRadius = radius
        this.bottomLeftRadius = radius
        buildRoundBackground()
    }

    override fun setRadii(radii: Array<Float>) {
        this.topLeftRadius = radii[0]
        this.topRightRadius = radii[1]
        this.bottomRightRadius = radii[2]
        this.bottomLeftRadius = radii[3]
        buildRoundBackground()
    }

    override fun setNormalBgColor(color: Int) {
        this.normalBgColor = color
        buildRoundBackground()
    }

    override fun setNormalBorderColor(color: Int) {
        this.normalBorderColor = color
        buildRoundBackground()
    }

    override fun setDisabledBgColor(color: Int) {
        this.disabledBgColor = color
        buildRoundBackground()
    }

    override fun setDisabledBorderColor(color: Int) {
        this.disabledBorderColor = color
        buildRoundBackground()
    }

    override fun setPressedBgColor(color: Int) {
        this.pressedBgColor = color
        buildRoundBackground()
    }

    override fun setPressedBorderColor(color: Int) {
        this.pressedBorderColor = color
        buildRoundBackground()
    }

    override fun setBorderWidth(size: Int) {
        this.borderWidth = size
        buildRoundBackground()
    }

    /**
     * 处理圆角背景
     */
    override fun buildRoundBackground() {
        val radii = FloatArray(8)
        radii[0] = topLeftRadius
        radii[1] = topLeftRadius
        radii[2] = topRightRadius
        radii[3] = topRightRadius
        radii[4] = bottomRightRadius
        radii[5] = bottomRightRadius
        radii[6] = bottomLeftRadius
        radii[7] = bottomLeftRadius

        val stateListDrawable = StateListDrawable()
        if (pressedBgColor != INVALID_COLOR || borderWidth > 0) {
            val pressedDrawable = GradientDrawable()
            pressedDrawable.shape = GradientDrawable.RECTANGLE
            pressedDrawable.cornerRadii = radii
            if (pressedBgColor != INVALID_COLOR) {
                pressedDrawable.setColor(pressedBgColor)
            }
            if (borderWidth > 0) {
                pressedDrawable.setStroke(borderWidth, pressedBorderColor)
            }
            stateListDrawable.addState(intArrayOf(android.R.attr.state_pressed), pressedDrawable)
        }
        if (disabledBgColor != INVALID_COLOR || borderWidth > 0) {
            val disabledDrawable = GradientDrawable()
            disabledDrawable.shape = GradientDrawable.RECTANGLE
            disabledDrawable.cornerRadii = radii
            if (pressedBgColor != INVALID_COLOR) {
                disabledDrawable.setColor(pressedBgColor)
            }
            if (borderWidth > 0) {
                disabledDrawable.setStroke(borderWidth, disabledBorderColor)
            }
            disabledDrawable.setStroke(borderWidth, disabledBorderColor)
            stateListDrawable.addState(intArrayOf(-android.R.attr.state_enabled), disabledDrawable)
        }
        val normalDrawable = GradientDrawable()
        normalDrawable.shape = GradientDrawable.RECTANGLE
        normalDrawable.cornerRadii = radii
        normalDrawable.setColor(normalBgColor)
        if (borderWidth > 0) {
            normalDrawable.setStroke(borderWidth, normalBorderColor)
        }
        stateListDrawable.addState(intArrayOf(), normalDrawable)
        view.background = stateListDrawable
    }
}
