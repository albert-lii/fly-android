package com.fly.example.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.fly.example.Constants
import com.fly.example.base.BaseAppViewModel
import org.we.fly.utils.livebus.LiveBus

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/12 7:47 PM
 * @description: --
 * @since: 1.0.0
 */

class LiveBusSecondViewModel : BaseAppViewModel() {
    var normalObserveData = MutableLiveData<String>()
    var stickyObserveData = MutableLiveData<String>()
    var foreverObserveData = MutableLiveData<String>()
    var stickyForeverObserveData = MutableLiveData<String>()

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

    fun postTestLifecycleMsg() {
        LiveBus.get(Constants.BK_SECOND_POST_CREATED_TEST)
            .alwaysBeActive(true)
            .post(null)
        LiveBus.get(Constants.BK_SECOND_POST_STARTED_TEST)
            .alwaysBeActive(false)
            .post(null)
    }

    fun postTestMsg() {
        LiveBus.get(Constants.BK_SECOND_POST_TEST).post("SecondActivity的Post测试 END")
        finishPage()
    }

    override fun onDestroy() {
        super.onDestroy()
        LiveBus.get(Constants.BK_SECOND_POST_TEST, String::class.java)
            .removeObserver(foreverObserver)
        LiveBus.get(Constants.BK_SECOND_POST_TEST, String::class.java)
            .removeObserver(stickyForeverObserver)
    }
}