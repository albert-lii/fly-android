package org.we.fly.base.ui

import androidx.lifecycle.*

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/7 10:30 PM
 * @description: ViewModel的基类
 * @since: 1.0.0
 */
abstract class BaseViewModel : ViewModel(), ViewModelLifecycle, ViewBehavior {
    // 是否显示loading视图
    var _loadingEvent = MutableLiveData<Boolean>()
    // 是否显示空白视图
    var _emptyPageEvent = MutableLiveData<Boolean>()

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
