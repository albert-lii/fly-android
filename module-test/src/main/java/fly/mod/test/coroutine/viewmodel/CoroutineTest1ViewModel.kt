package fly.mod.test.coroutine.viewmodel

import androidx.lifecycle.MutableLiveData
import fly.lib.common.base.BaseAppViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/3/19 10:56 AM
 * @description: -
 * @since: 1.0.0
 */
class CoroutineTest1ViewModel : BaseAppViewModel() {
    private val INIT_VALUE = "----"
    val END_FLAG = "总耗时"
    var test1Result = MutableLiveData<String>(INIT_VALUE)
    var test2Result = MutableLiveData<String>(INIT_VALUE)
    var test3Result = MutableLiveData<String>(INIT_VALUE)
    var test4Result = MutableLiveData<String>(INIT_VALUE)

    fun test1() {
        launchOnUI {
            test1Result.value = "测试开始>>>>>>"
            val startTime = System.currentTimeMillis()
            val a1 = async {
                test1Result.value = test1Result.value + "\nasync1 --> delay(1000)"
                delay(1000)
            }
            val a2 = async {
                test1Result.value = test1Result.value + "\nasync2 --> delay(1000)"
                delay(1000)
            }
            test1Result.value = test1Result.value + "\nawait1 --> 开始执行"
            a1.await()
            test1Result.value = test1Result.value + "\nawait2 --> 开始执行"
            a2.await()
            val totalTime = System.currentTimeMillis() - startTime
            test1Result.value = test1Result.value + "\n${END_FLAG}：${totalTime}ms"
        }
    }

    fun test2() {
        launchOnUI {
            test2Result.value = "测试开始>>>>>>"
            val startTime = System.currentTimeMillis()
            val a1 = async {
                test2Result.value = test2Result.value + "\nasync1 --> delay(1000)"
                delay(1000)
            }
            test2Result.value = test2Result.value + "\nawait1 --> 开始执行"
            a1.await()
            val a2 = async {
                test2Result.value = test2Result.value + "\nasync2 --> delay(1000)"
                delay(1000)
            }
            test2Result.value = test2Result.value + "\nawait2 --> 开始执行"
            a2.await()
            val totalTime = System.currentTimeMillis() - startTime
            test2Result.value = test2Result.value + "\n${END_FLAG}：${totalTime}ms"
        }
    }

    fun test3() {
        launchOnUI {
            test3Result.value = "测试开始>>>>>>"
            val startTime = System.currentTimeMillis()
            val a1 = async {
                test3Result.value = test3Result.value + "\nasync1 --> delay(1000)"
                delay(1000)
            }
            val a2 = async {
                test3Result.value = test3Result.value + "\nasync2 --> delay(1000)"
                delay(1000)
            }
            test3Result.value = test3Result.value + "\nawait1 --> 开始执行"
            a1.await()
            test3Result.value = test3Result.value + "\nawait2 --> 开始执行"
            a2.await()
            val a3 = async {
                test3Result.value = test3Result.value + "\nasync3 --> delay(1000)"
                delay(1000)
            }
            test3Result.value = test3Result.value + "\nawait3 --> 开始执行"
            a3.await()
            val totalTime = System.currentTimeMillis() - startTime
            test3Result.value = test3Result.value + "\n${END_FLAG}：${totalTime}ms"
        }
    }

    fun test4() {
        launchOnUI {
            test4Result.value = "测试开始>>>>>>"
            val startTime = System.currentTimeMillis()
            val a1 = async {
                test4Result.value = test4Result.value + "\nasync1 --> delay(1000)"
                delay(1000)
            }
            test4Result.value = test4Result.value + "\nawait1 --> 开始执行"
            a1.await()
            val a2 = async {
                test4Result.value = test4Result.value + "\nasync2 --> delay(1000)"
                delay(1000)
            }
            test4Result.value = test4Result.value + "\nawait2 --> 开始执行"
            a2.await()
            val a3 = async {
                test4Result.value = test4Result.value + "\nasync3 --> delay(1000)"
                delay(1000)
            }
            test4Result.value = test4Result.value + "\nawait3 --> 开始执行"
            a3.await()
            val totalTime = System.currentTimeMillis() - startTime
            test4Result.value = test4Result.value + "\n${END_FLAG}：${totalTime}ms"
        }
    }
}