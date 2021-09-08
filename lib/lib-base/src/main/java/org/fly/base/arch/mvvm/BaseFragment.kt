package org.fly.base.arch.mvvm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import org.fly.base.utils.ToastUtils

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/19 9:42 PM
 * @description: Fragment的基类
 * @since: 1.0.0
 */
abstract class BaseFragment : Fragment(), ILazyLoad, ViewBehavior {
    /**
     * 缓存视图，如果视图已经创建，则不再初始化视图
     */
    private var rootView: View? = null

    /**
     * 当前的状态
     */
    private var currentState = ILazyLoad.ON_START

    /**
     * 是否开启懒加载，默认开启
     */
    private var isOpenLazyInit = true

    /**
     * 是否已经执行懒加载
     */
    private var hasLazyInit = false

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
     * 懒加载的调用时机
     */
    protected fun getLazyLoadtState() = ILazyLoad.ON_RESUME

    protected fun setCurrentState(state: Int) {
        this.currentState = state
    }

    /**
     * 是否开启懒加载，调用此方法建议在getLazyInitState()所返回的状态之前
     */
    protected fun openLazyInit(isOpen: Boolean) {
        this.isOpenLazyInit = isOpen
    }

    /**
     * 调用懒加载
     *
     * @param callInUserVisibleHint 是否是在setUserVisibleHint中调用
     */
    protected fun excuteLazyInit(callInUserVisibleHint: Boolean) {
        if (!callInUserVisibleHint) {
            if (!isCallUserVisibleHint) isVisibleToUser = !isHidden
        }
        if (isOpenLazyInit && !hasLazyInit && isVisibleToUser && currentState >= getLazyLoadtState()) {
            hasLazyInit = true
            lazyInit()
        }
    }

    protected fun getRootView(): View? {
        return rootView
    }

    protected fun setRootView(view: View) {
        this.rootView = view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setCurrentState(ILazyLoad.ON_ATTACH)
        excuteLazyInit(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCurrentState(ILazyLoad.ON_CREATE)
        excuteLazyInit(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setCurrentState(ILazyLoad.ON_CREATE_VIEW)
        if (rootView != null) {
            return rootView
        }
        rootView = inflater.inflate(getLayoutId(), container, false)
        initialize(savedInstanceState)
        excuteLazyInit(false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setCurrentState(ILazyLoad.ON_ACTIVITY_CREATED)
        excuteLazyInit(false)
    }

    override fun onStart() {
        super.onStart()
        setCurrentState(ILazyLoad.ON_START)
        excuteLazyInit(false)
    }

    override fun onResume() {
        super.onResume()
        setCurrentState(ILazyLoad.ON_RESUME)
        excuteLazyInit(false)
    }

    /**
     * 主要针对add+show+hide模式下，Fragment的隐藏与显示调用的方法
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        isVisibleToUser = !hidden
        excuteLazyInit(false)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser
        isCallUserVisibleHint = true
        excuteLazyInit(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hasLazyInit = false
        isVisibleToUser = false
        isCallUserVisibleHint = false
    }

    protected abstract @LayoutRes
    fun getLayoutId(): Int

    /**
     *  初始化操作
     */
    protected abstract fun initialize(savedInstanceState: Bundle?)

    override fun lazyInit() {

    }

    protected fun showToast(text: String, showLong: Boolean = false) {
        showToast(ToastEvent(content = text, showLong = showLong))
    }

    protected fun showToast(@StringRes resId: Int, showLong: Boolean = false) {
        showToast(ToastEvent(contentResId = resId, showLong = showLong))
    }

    override fun showToast(event: ToastEvent) {
        if (event.content != null) {
            ToastUtils.showToast(requireContext(), event.content!!, event.showLong)
        } else if (event.contentResId != null) {
            ToastUtils.showToast(requireContext(), getString(event.contentResId!!), event.showLong)
        }
    }

    override fun navigate(page: Any) {
        startActivity(Intent(requireContext(), page as Class<*>))
    }

    override fun backPress(arg: Any?) {
        activity?.onBackPressed()
    }

    override fun finishPage(arg: Any?) {
        activity?.finish()
    }
}