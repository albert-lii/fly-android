package org.we.fly.base.mvvm

import androidx.annotation.StringRes
import androidx.lifecycle.*
import org.we.fly.base.BaseConstants

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

    var _toastEvent = MutableLiveData<Map<String, *>>()
        private set

    // 输入字符串，弹出toast提示Event
    var _toastStrEvent = MutableLiveData<Array<Any>>()
        private set

    // 输入字符串ResId，弹出toast提示Event
    var _toastStrIdEvent = MutableLiveData<Array<Int>>()
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

    private lateinit var lifcycleOwner: LifecycleOwner

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

    override fun showToast(str: String, duration: Int) {
        _toastStrEvent.postValue(arrayOf(str, duration))
        val map = HashMap<String, Any>()
        map[BaseConstants.TOAST_KEY_CONTENT] = str
        map[BaseConstants.TOAST_KEY_DURATION] = duration
    }

    override fun showToast(@StringRes strId: Int, duration: Int) {
        _toastStrIdEvent.postValue(arrayOf(strId, duration))
    }

    override fun navigateTo(page: Any) {
        _pageNavigationEvent.postValue(page)
    }

    override fun backPress(arg: Any?) {
        _backPressEvent.postValue(arg)
    }

    override fun finishPage(arg: Any?) {
        _finishPageEvent.postValue(arg)
    }

    protected fun showToast(map: Map<String, *>) {
        _toastEvent.postValue(map)
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
