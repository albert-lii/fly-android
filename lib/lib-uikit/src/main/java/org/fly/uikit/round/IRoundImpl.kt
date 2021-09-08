package org.fly.uikit.round

import android.content.res.TypedArray

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/9/6 10:51 上午
 * @description: -
 * @since: 1.0.0
 */
interface IRoundImpl : IRound {
    var helper: RHelper

    fun initRoundAttrs(ta: TypedArray) {
        helper.initAttrs(ta)
    }

    override fun setTopLeftRadius(radius: Float) {
        helper.setTopLeftRadius(radius)
    }

    override fun setTopRightRadius(radius: Float) {
        helper.setTopRightRadius(radius)
    }

    override fun setBottomLeftRadius(radius: Float) {
        helper.setBottomLeftRadius(radius)
    }

    override fun setBottomRightRadius(radius: Float) {
        helper.setBottomRightRadius(radius)
    }

    override fun setRadius(radius: Float) {
        helper.setRadius(radius)
    }

    override fun setNormalColor(color: Int) {
        helper.setNormalColor(color)
    }

    override fun setDisabledColor(color: Int) {
        helper.setDisabledColor(color)
    }

    override fun setPressedColor(color: Int) {
        helper.setPressedColor(color)
    }

    override fun setBorderWidth(size: Int) {
        helper.setBorderWidth(size)
    }

    override fun setNormalBorderColor(color: Int) {
        helper.setNormalBorderColor(color)
    }

    override fun setDisabledBorderColor(color: Int) {
        helper.setDisabledBorderColor(color)
    }

    override fun setPressedBorderColor(color: Int) {
        helper.setPressedBorderColor(color)
    }

    override fun processRoundBackground() {
        helper.processRoundBackground()
    }
}