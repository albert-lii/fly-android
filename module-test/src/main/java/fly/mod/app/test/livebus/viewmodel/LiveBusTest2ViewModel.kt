package fly.mod.app.test.livebus.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import fly.mod.app.test.livebus.Constants
import fly.mod.lib.common.base.BaseAppViewModel
import org.fly.base.communication.livebus.LiveBus

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/12 7:47 PM
 * @description: --
 * @since: 1.0.0
 */

class LiveBusTest2ViewModel : BaseAppViewModel() {
    var normalObserveData = MutableLiveData<String>("普通订阅：")
    var stickyObserveData = MutableLiveData<String>("Sticky订阅：")
    var foreverObserveData = MutableLiveData<String>("Forever订阅：")
    var stickyForeverObserveData = MutableLiveData<String>("StickyForever订阅：")

    var foreverObserver = object : Observer<String> {
        override fun onChanged(t: String?) {
            foreverObserveData.value = "Forever订阅：${t}"
        }
    }

    var stickyForeverObserver = object : Observer<String> {
        override fun onChanged(t: String?) {
            stickyForeverObserveData.value = "StickyForever订阅：${t}"
        }
    }

    fun postMsgToTest1() {
        LiveBus.get(Constants.LK_TEST2_POST).post("收到来自Test2的消息")
        finishPage()
    }

    override fun onDestroy() {
        super.onDestroy()
        LiveBus.get(Constants.LK_TEST2_POST, String::class.java)
            .removeObserver(foreverObserver)
        LiveBus.get(Constants.LK_TEST2_POST, String::class.java)
            .removeObserver(stickyForeverObserver)
    }
}