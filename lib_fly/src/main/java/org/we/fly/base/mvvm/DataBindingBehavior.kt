package org.we.fly.base.mvvm

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/8 10:19 AM
 * @description: --
 * @since: 1.0.0
 */
interface DataBindingBehavior {

    fun getViewModelVariableId(): Int

    companion object {
        const val NO_VIEW_MODEL = -1
    }
}