package org.fly.uikit.round

import android.view.View

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/30 10:23 下午
 * @description: 圆角相关属性
 * @since: 1.0.0
 */
interface IRound {

    fun setTopLeftRadius(radius: Float)

    fun setTopRightRadius(radius: Float)

    fun setBottomLeftRadius(radius: Float)

    fun setBottomRightRadius(radius: Float)

    fun setRadius(radius: Float)

    fun setNormalColor(color: Int)

    fun setDisabledColor(color: Int)

    fun setPressedColor(color: Int)

    fun setBorderWidth(size: Int)

    fun setNormalBorderColor(color: Int)

    fun setDisabledBorderColor(color: Int)

    fun setPressedBorderColor(color: Int)

    /**
     * 处理圆角背景
     */
    fun processRoundBackground()
}