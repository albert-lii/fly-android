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

    override fun setRadii(radii: Array<Float>) {
        helper.setRadii(radii)
    }

    override fun setNormalBgColor(color: Int) {
        helper.setNormalBgColor(color)
    }

    override fun setNormalBorderColor(color: Int) {
        helper.setNormalBorderColor(color)
    }

    override fun setDisabledBgColor(color: Int) {
        helper.setDisabledBgColor(color)
    }

    override fun setDisabledBorderColor(color: Int) {
        helper.setDisabledBorderColor(color)
    }

    override fun setPressedBgColor(color: Int) {
        helper.setPressedBgColor(color)
    }

    override fun setPressedBorderColor(color: Int) {
        helper.setPressedBorderColor(color)
    }

    override fun setBorderWidth(size: Int) {
        helper.setBorderWidth(size)
    }

    override fun buildRoundBackground() {
        helper.buildRoundBackground()
    }
}