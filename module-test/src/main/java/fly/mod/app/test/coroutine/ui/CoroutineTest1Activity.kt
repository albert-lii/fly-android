package fly.mod.app.test.coroutine.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import fly.mod.app.test.R
import fly.mod.app.test.coroutine.viewmodel.CoroutineTest1ViewModel
import fly.mod.app.test.databinding.StActivityCoroutineTest1Binding
import fly.mod.lib.common.base.BaseAppBVMActivity
import fly.mod.lib.common.router.RouteConstants
import org.fly.base.extensions.observeNonNull
import org.fly.base.extensions.singleClick

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/3/19 10:43 AM
 * @description: async的顺序执行与并发执行测试
 * @since: 1.0.0
 */
@Route(path = RouteConstants.PAGE_ST_COROUTINE_TEST1)
class CoroutineTest1Activity :
    BaseAppBVMActivity<StActivityCoroutineTest1Binding, CoroutineTest1ViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.st_activity_coroutine_test1
    }

    override fun createViewModel(): CoroutineTest1ViewModel {
        return CoroutineTest1ViewModel()
    }

    override fun initialize(savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        addListener()
        addObserver()
    }

    private fun addListener() {
        binding.btn1.singleClick {
            binding.btn1.isEnabled = false
            viewModel.test1()
        }
        binding.btn2.singleClick {
            binding.btn2.isEnabled = false
            viewModel.test2()
        }
        binding.btn3.singleClick {
            binding.btn3.isEnabled = false
            viewModel.test3()
        }
        binding.btn4.singleClick {
            binding.btn4.isEnabled = false
            viewModel.test4()
        }
    }

    private fun addObserver() {
        viewModel.test1Result.observeNonNull(this) {
            if (it.contains(viewModel.END_FLAG)) {
                binding.btn1.isEnabled = true
            }
        }
        viewModel.test2Result.observeNonNull(this) {
            if (it.contains(viewModel.END_FLAG)) {
                binding.btn2.isEnabled = true
            }
        }
        viewModel.test3Result.observeNonNull(this) {
            if (it.contains(viewModel.END_FLAG)) {
                binding.btn3.isEnabled = true
            }
        }
        viewModel.test4Result.observeNonNull(this) {
            if (it.contains(viewModel.END_FLAG)) {
                binding.btn4.isEnabled = true
            }
        }
    }
}