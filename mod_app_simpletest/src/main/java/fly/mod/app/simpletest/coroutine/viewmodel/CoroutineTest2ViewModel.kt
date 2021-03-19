package fly.mod.app.simpletest.coroutine.viewmodel

import androidx.lifecycle.MutableLiveData
import fly.mod.lib.common.base.BaseAppViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/3/19 12:01 PM
 * @description: -
 * @since: 1.0.0
 */
class CoroutineTest2ViewModel : BaseAppViewModel() {
    private val INIT_VALUE = "----"
    var test1Result = MutableLiveData<String>(INIT_VALUE)
    var test2Result = MutableLiveData<String>(INIT_VALUE)
    var test3Result = MutableLiveData<String>(INIT_VALUE)
    var test4Result = MutableLiveData<String>(INIT_VALUE)

    fun test1() {
        test1Result.value = "测试开始>>>>>>"
        test1Result.value = test1Result.value + "\n父协程 --> 已准备"
        launchOnUI {
            test1Result.value = test1Result.value + "\n父协程 --> 已启动"
            val a = async {
                test1Result.value = test1Result.value + "\nasync --> delay(2000)"
                delay(2000)
                test1Result.value = test1Result.value + "\nasync --> 执行完毕"
            }
            test1Result.value = test1Result.value + "\nasync --> 启动"
            a.await()
        }
        test1Result.value = test1Result.value + "\n主线程 --> sleep(500)"
        Thread.sleep(500)
        test1Result.value = test1Result.value + "\n主线程 --> 执行完毕"
    }

    fun test2() {
        test2Result.value = "测试开始>>>>>>"
        test2Result.value = test2Result.value + "\n父协程 --> 已准备"
        val job = launchOnUI {
            test2Result.value = test2Result.value + "\n父协程 --> 已启动"
            val a = async {
                test2Result.value = test2Result.value + "\nasync --> delay(2000)"
                delay(2000)
                test2Result.value = test2Result.value + "\nasync --> 执行完毕"
            }
            test2Result.value = test2Result.value + "\nasync --> 启动"
            a.await()
        }
        test2Result.value = test2Result.value + "\n主线程 --> sleep(500)"
        Thread.sleep(500)
        job.cancel()
        test2Result.value = test2Result.value + "\n父协程 --> 已取消"
        test2Result.value = test2Result.value + "\n主线程 --> 执行完毕"
    }

    fun test3() {
        test3Result.value = "测试开始>>>>>>"
        launchOnUI {
            val a1 = async {
                test3Result.value = test3Result.value + "\nasync1 --> delay(1000)"
                delay(1000)
                test3Result.value = test3Result.value + "\nasync1 --> 执行完毕"
            }
            val a2 = async {
                test3Result.value = test3Result.value + "\nasync2 --> delay(2000)"
                delay(2000)
                test3Result.value = test3Result.value + "\nasync2 --> 执行完毕"
            }
            a1.await()
            a2.await()
        }
    }

    fun test4() {
        test4Result.value = "测试开始>>>>>>"
        launchOnUI {
            val a1 = async {
                test4Result.value = test4Result.value + "\nasync1 --> delay(1000)"
                delay(1000)
                test4Result.value = test4Result.value + "\nasync1 --> 执行完毕"
            }
            val a2 = async {
                test4Result.value = test4Result.value + "\nasync2 --> 取消async1"
                a1.cancel()
                test4Result.value = test4Result.value + "\nasync2 --> delay(2000)"
                delay(2000)
                test3Result.value = test4Result.value + "\nasync2 --> 执行完毕"
            }
            a1.await()
            a2.await()
        }
    }
}