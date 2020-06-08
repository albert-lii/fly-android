package org.we.fly.base.ui

import androidx.fragment.app.Fragment

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/5/30 6:34 PM
 * @description: 懒加载的Fragment
 * @since: 1.0.0
 */
abstract class BaseLazyFragment : Fragment() {

    /**
     * 是否执行懒加载
     */
    private var isLoaded = false

    /**
     * 当前Fragment是否对用户可见
     */
    private var isVisibleToUser = false

    /**
     * 是否调用了setUserVisibleHint方法。
     * 处理show+add+hide模式下，默认可见Fragment不调用onHiddenChanged方法，进而不执行懒加载方法的问题。
     */
    private var isCallUserVisibleHint = false

    /**
     * 是否调用了onResume方法。
     * 当使用ViewPager+Fragment形式时会调用setUserVisibleHint，该方法会优先Fragment生命周期函数调用，
     * 所以这个时候就会导致在setUserVisibleHint方法执行时就执行了懒加载，而不是在onResume方法实际调用的时候执行懒加载。
     * 所以需要这个变量来判断是否已经执行了onResume方法。
     */
    private var isCallResume = false

    private fun judgeLazyInit() {
        if (!isLoaded && isVisibleToUser && isCallResume) {
            lazyInit()
        }
    }

    override fun onResume() {
        super.onResume()
        isCallResume = true
        if (!isCallUserVisibleHint) isVisibleToUser = !isHidden
        judgeLazyInit()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        isVisibleToUser = !hidden
        judgeLazyInit()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser
        isCallUserVisibleHint = true
        judgeLazyInit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded = false
        isVisibleToUser = false
        isCallUserVisibleHint = false
        isCallResume = false
    }

    abstract fun lazyInit()
}