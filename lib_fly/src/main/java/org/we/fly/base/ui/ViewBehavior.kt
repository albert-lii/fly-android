package org.we.fly.base.ui

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/9 8:01 PM
 * @description: 页面的常用操作
 * @since: 1.0.0
 */
interface ViewBehavior {
    /**
     * 是否显示loading视图
     */
    fun showLoadingUI(isShow: Boolean)

    /**
     * 是否显示空白视图
     */
    fun showEmptyUI(isShow: Boolean)
}