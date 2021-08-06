package fly.mod.app.test.coroutine.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import fly.mod.app.test.R
import fly.mod.app.test.coroutine.viewmodel.CoroutineTest2ViewModel
import fly.mod.app.test.databinding.StActivityCoroutineTest2Binding
import fly.mod.lib.common.base.BaseAppBVMActivity
import fly.mod.lib.common.router.RouteConstants
import org.fly.base.extensions.singleClick

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/3/19 12:00 PM
 * @description: 父协程取消，async还会继续执行吗
 * @since: 1.0.0
 */
@Route(path = RouteConstants.PAGE_ST_COROUTINE_TEST2)
class CoroutineTest2Activity :
    BaseAppBVMActivity<StActivityCoroutineTest2Binding, CoroutineTest2ViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.st_activity_coroutine_test2
    }

    override fun createViewModel(): CoroutineTest2ViewModel {
        return CoroutineTest2ViewModel()
    }

    override fun initialize(savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        addListener()
    }

    private fun addListener() {
        binding.btn1.singleClick {
            it.isEnabled = false
            viewModel.test1()
            it.postDelayed({
                it.isEnabled = true
            }, 2800L)
        }
        binding.btn2.singleClick {
            it.isEnabled = false
            viewModel.test2()
            it.postDelayed({
                it.isEnabled = true
            }, 2800)
        }
        binding.btn3.singleClick {
            it.isEnabled = false
            viewModel.test3()
            it.postDelayed({
                it.isEnabled = true
            }, 2800)
        }
        binding.btn4.singleClick {
            it.isEnabled = false
            viewModel.test4()
            it.postDelayed({
                it.isEnabled = true
            }, 2800)
        }
    }
}