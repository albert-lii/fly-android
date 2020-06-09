package org.we.fly.base.ui

import androidx.annotation.IntDef

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

        @IntDef(NO_VIEW_MODEL)
        @Retention(AnnotationRetention.SOURCE)
        annotation class ViewModelVariableId
    }
}