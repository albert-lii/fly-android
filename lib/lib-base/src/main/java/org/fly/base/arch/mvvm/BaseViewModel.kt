package org.fly.base.arch.mvvm

import android.annotation.SuppressLint
import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/7 10:30 PM
 * @description: ViewModel的基类
 * @since: 1.0.0
 */
abstract class BaseViewModel : ViewModel(), ViewModelLifecycle, ViewBehavior {

    // loading视图显示Event
    var _loadingEvent = MutableLiveData<Boolean>()
        private set

    // 无数据视图显示Event
    var _emptyPageEvent = MutableLiveData<Boolean>()
        private set

    // toast提示Event
    var _toastEvent = MutableLiveData<Map<String, *>>()
        private set

    // 不带参数的页面跳转Event
    var _pageNavigationEvent = MutableLiveData<Any>()
        private set

    // 点击系统返回键Event
    var _backPressEvent = MutableLiveData<Any?>()
        private set

    // 关闭页面Event
    var _finishPageEvent = MutableLiveData<Any?>()
        private set

    @SuppressLint("StaticFieldLeak")
    lateinit var application: Application

    private lateinit var lifcycleOwner: LifecycleOwner

    /**
     * 在主线程中执行一个协程
     */
    protected fun launchOnUI(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(Dispatchers.Main) { block() }
    }

    /**
     * 在IO线程中执行一个协程
     */
    protected fun launchOnIO(block: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch(Dispatchers.IO) { block() }
    }

    override fun onAny(owner: LifecycleOwner, event: Lifecycle.Event) {
        this.lifcycleOwner = owner
    }

    override fun onCreate() {

    }

    override fun onStart() {

    }

    override fun onResume() {

    }

    override fun onPause() {

    }

    override fun onStop() {

    }

    override fun onDestroy() {

    }

    override fun showLoadingUI(isShow: Boolean) {
        _loadingEvent.postValue(isShow)
    }

    override fun showEmptyUI(isShow: Boolean) {
        _emptyPageEvent.postValue(isShow)
    }

    override fun showToast(map: Map<String, *>) {
        _toastEvent.postValue(map)
    }

    override fun navigate(page: Any) {
        _pageNavigationEvent.postValue(page)
    }

    override fun backPress(arg: Any?) {
        _backPressEvent.postValue(arg)
    }

    override fun finishPage(arg: Any?) {
        _finishPageEvent.postValue(arg)
    }

    protected fun showToast(msg: String, duration: Int? = null) {
        val map = HashMap<String, Any>().apply {
            put(
                FlyBaseConstants.FLY_TOAST_KEY_CONTENT_TYPE,
                FlyBaseConstants.FLY_TOAST_CONTENT_TYPE_STR
            )
            put(FlyBaseConstants.FLY_TOAST_KEY_CONTENT, msg)
            if (duration != null) {
                put(FlyBaseConstants.FLY_TOAST_KEY_DURATION, duration)
            }
        }
        showToast(map)
    }

    protected fun showToast(@StringRes resId: Int, duration: Int? = null) {
        val map = HashMap<String, Any>().apply {
            put(
                FlyBaseConstants.FLY_TOAST_KEY_CONTENT_TYPE,
                FlyBaseConstants.FLY_TOAST_CONTENT_TYPE_RESID
            )
            put(FlyBaseConstants.FLY_TOAST_KEY_CONTENT, resId)
            if (duration != null) {
                put(FlyBaseConstants.FLY_TOAST_KEY_DURATION, duration)
            }
        }
        showToast(map)
    }

    protected fun backPress() {
        backPress(null)
    }

    protected fun finishPage() {
        finishPage(null)
    }

    companion object {
        @JvmStatic
        fun <T : BaseViewModel> createViewModelFactory(viewModel: T): ViewModelProvider.Factory {
            return ViewModelFactory(viewModel)
        }
    }
}


/**
 * 创建ViewModel的工厂，以此方法创建的ViewModel，可在构造函数中传参
 */
class ViewModelFactory(val viewModel: BaseViewModel) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return viewModel as T
    }
}
