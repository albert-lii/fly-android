package fly.mod.app.simpletest.livebus.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import fly.mod.app.simpletest.livebus.Constants
import fly.mod.app.simpletest.livebus.ui.LiveBusTest2Activity
import fly.mod.lib.common.base.BaseAppViewModel
import org.we.fly.utils.livebus.LiveBus

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/11 8:08 PM
 * @description: --
 * @since: 1.0.0
 */
class LiveBusTest1ViewModel : BaseAppViewModel() {
    var recevieCountOnCREATED = 0
    var recevieCountOnSTARTED = 0

    var receiveCountOnCreatedData = MutableLiveData<String>()
    var receiveCountOnStartedData = MutableLiveData<String>()

    var normalObserveData = MutableLiveData<String>("普通订阅：")
    var stickyObserveData = MutableLiveData<String>("Sticky订阅：")
    var foreverObserveData = MutableLiveData<String>("Forever订阅：")
    var stickyForeverObserveData = MutableLiveData<String>("StickyForever订阅：")

    var foreverObserver = object : Observer<String> {
        override fun onChanged(t: String?) {
            foreverObserveData.value = "forever订阅：${t}"
        }
    }
    var stickyForeverObserver = object : Observer<String> {
        override fun onChanged(t: String?) {
            stickyForeverObserveData.value = "StickyForever订阅：${t}"
        }
    }

    fun postMsgToTest2() {
        recevieCountOnCREATED = 0
        recevieCountOnSTARTED = 0
        LiveBus.get(Constants.LK_TEST1_POST).post("收到来自Test1的消息")
    }

    override fun onDestroy() {
        super.onDestroy()
        LiveBus.get(Constants.LK_TEST2_POST, String::class.java)
            .removeObserver(foreverObserver)
        LiveBus.get(Constants.LK_TEST2_POST, String::class.java)
            .removeObserver(stickyForeverObserver)
    }
}