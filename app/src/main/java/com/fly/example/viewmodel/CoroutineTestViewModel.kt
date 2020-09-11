package com.fly.example.viewmodel

import androidx.lifecycle.MutableLiveData
import com.fly.example.base.BaseAppViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.supervisorScope
import okhttp3.internal.wait
import java.io.IOException
import java.lang.Exception

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/9/11 1:02 PM
 * @description: -
 * @since: 1.0.0
 */
class CoroutineTestViewModel : BaseAppViewModel() {
    var q1TestResult = MutableLiveData<String>()
    var q2TestResult = MutableLiveData<String>()
    var q3TestResult = MutableLiveData<String>()
    var q4TestResult = MutableLiveData<String>()
    var q5TestResult = MutableLiveData<String>()
    var q6TestResult = MutableLiveData<String>()
    var q7TestResult = MutableLiveData<String>()
    var q8TestResult = MutableLiveData<String>()

    /**========================================================
     * 测试 async 的顺序请求与并发
     **=======================================================*/

    fun q1Test() {
        launchOnUI {
            val startTime = System.currentTimeMillis()
            val a1 = async { delay(1000) }
            val a2 = async { delay(1000) }
            a1.await()
            a2.await()
            val totalTime = System.currentTimeMillis() - startTime
            q1TestResult.value = "耗时：${totalTime}ms"
        }
    }

    fun q2Test() {
        launchOnUI {
            val startTime = System.currentTimeMillis()
            val a1 = async { delay(1000) }
            a1.await()
            val a2 = async { delay(1000) }
            a2.await()
            val totalTime = System.currentTimeMillis() - startTime
            q2TestResult.value = "耗时：${totalTime}ms"
        }
    }

    fun q3Test() {
        launchOnUI {
            val startTime = System.currentTimeMillis()
            val a1 = async { delay(1000) }
            val a2 = async { delay(1000) }
            a1.await()
            a2.await()
            val a3 = async { delay(1000) }
            a3.await()
            val totalTime = System.currentTimeMillis() - startTime
            q3TestResult.value = "耗时：${totalTime}ms"
        }
    }

    fun q4Test() {
        launchOnUI {
            val startTime = System.currentTimeMillis()
            val a1 = async { delay(1000) }
            a1.await()
            val a2 = async { delay(1000) }
            a2.await()
            val a3 = async { delay(1000) }
            a3.await()
            val totalTime = System.currentTimeMillis() - startTime
            q4TestResult.value = "耗时：${totalTime}ms"
        }
    }

    /**========================================================
     * 测试 async 的顺序请求与并发
     **=======================================================*/

    fun q5Test() {
        val job = launchOnUI {
            val a1 = async {
                q5TestResult.value = "async开始执行"
                delay(3000)
                q5TestResult.value = "async执行完毕"
            }
            a1.await()
        }
        Thread.sleep(1000)
        job.cancel()
        q5TestResult.value = "job已取消"
    }

    fun q6Test() {
        val job = launchOnUI {
            val a1 = async {
                q6TestResult.value = "async开始执行"
                delay(3000)
                q6TestResult.value = "async执行完毕"
            }
            a1.await()
        }
        Thread.sleep(1000)
        q6TestResult.value = "等待async执行完毕"
    }

    fun q7Test() {
        launchOnUI {
            val a1 = async {
                q7TestResult.value = "a1开始执行"
                delay(1000)
                q7TestResult.value = "a1执行完毕"
            }
            val a2 = async {
                a1.cancel()
                q7TestResult.value = "a2开始执行"
                delay(2000)
                q7TestResult.value = "a2执行完毕"
            }
            a1.await()
            a2.await()
        }
    }

    fun q8Test() {
        launchOnUI {
            val a1 = async(coroutineContext) {
                q8TestResult.value = "a1开始执行"
                delay(1000)
                q8TestResult.value = "a1执行完毕"
            }
            val a2 = async(coroutineContext) {
                q8TestResult.value = "a2开始执行"
                delay(2000)
                q8TestResult.value = "a2执行完毕"
            }
            a1.await()
            a2.await()
        }
    }
}