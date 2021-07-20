package org.fly.base.arch.mvvm

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/19 9:33 PM
 * @description: Fragment懒加载接口
 * @since: 1.0.0
 */
interface ILazyLoad {
    companion object {
        const val ON_ATTACH = 1
        const val ON_CREATE = 2
        const val ON_CREATE_VIEW = 3
        const val ON_ACTIVITY_CREATED = 4
        const val ON_START = 5
        const val ON_RESUME = 6
        const val ANY = 7
    }

    /**
     * 延迟加载
     */
    fun lazyLoad()
}