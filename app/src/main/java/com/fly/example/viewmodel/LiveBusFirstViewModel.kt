package com.fly.example.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.fly.example.Constants
import com.fly.example.base.BaseAppViewModel
import com.fly.example.ui.livebustest.LiveBusSecondActivity
import org.we.fly.utils.livebus.LiveBus

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/11 8:08 PM
 * @description: --
 * @since: 1.0.0
 */
class LiveBusFirstViewModel : BaseAppViewModel() {
    var recevieCountOnCREATED = 0
    var recevieCountOnSTARTED = 0

    var receiveCountOnCreatedData = MutableLiveData<String>()
    var receiveCountOnStartedData = MutableLiveData<String>()
    var normalObserveData = MutableLiveData<String>()
    var stickyObserveData = MutableLiveData<String>()
    var foreverObserveData = MutableLiveData<String>()
    var stickyForeverObserveData = MutableLiveData<String>()

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

    fun postTestMsg() {
        recevieCountOnCREATED = 0
        recevieCountOnSTARTED = 0
        LiveBus.get(Constants.BK_FIRST_POST_TEST).post("FirstActivity中Post测试")
        navigateTo(LiveBusSecondActivity::class.java)
    }

    fun changeReceiveCountUI() {
        receiveCountOnCreatedData.value = "CREATED状态：收到来自Second页面的Post次数：${recevieCountOnCREATED}次"
        receiveCountOnStartedData.value = "STARTED状态：收到来自Second页面的Post次数：${recevieCountOnSTARTED}次"
    }


    override fun onDestroy() {
        super.onDestroy()
        LiveBus.get(Constants.BK_SECOND_POST_TEST, String::class.java)
            .removeObserver(foreverObserver)
        LiveBus.get(Constants.BK_SECOND_POST_TEST, String::class.java)
            .removeObserver(stickyForeverObserver)
    }
}