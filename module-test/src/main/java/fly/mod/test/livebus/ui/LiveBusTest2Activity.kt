package fly.mod.test.livebus.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import fly.mod.test.livebus.Constants
import fly.mod.test.R
import fly.mod.test.databinding.StActivityLivebusTest2Binding
import fly.mod.test.livebus.viewmodel.LiveBusTest2ViewModel
import fly.lib.common.base.BaseAppBVMActivity
import fly.lib.common.router.RouteConstants
import org.fly.base.signal.livebus.LiveBus
import org.fly.base.exts.singleClick


/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/3 2:29 PM
 * @description: LiveBus测试页面2
 * @since: 1.0.0
 */
@Route(path = RouteConstants.PAGE_ST_LIVEBUS_TEST2)
class LiveBusTest2Activity :
    BaseAppBVMActivity<StActivityLivebusTest2Binding, LiveBusTest2ViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.st_activity_livebus_test2
    }

    override fun createViewModel(): LiveBusTest2ViewModel {
        return LiveBusTest2ViewModel()
    }

    override fun initialize(savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        addListener()
        addObserve()
    }

    private fun addListener() {
        binding.btnPostEnd.singleClick {
            viewModel.postMsgToTest1()
        }
    }

    private fun addObserve() {
        LiveBus.get(Constants.LK_TEST1_POST, String::class.java)
            .observe(this, object : Observer<String> {
                override fun onChanged(t: String?) {
                    viewModel.normalObserveData.value = "普通订阅：${t}"
                }

            })
        LiveBus.get(Constants.LK_TEST1_POST, String::class.java)
            .observeSticky(this, object : Observer<String> {
                override fun onChanged(t: String?) {
                    viewModel.stickyObserveData.value = "Sticky订阅：${t}"
                }
            })
        LiveBus.get(Constants.LK_TEST1_POST, String::class.java)
            .observeForever(viewModel.foreverObserver)
        LiveBus.get(Constants.LK_TEST1_POST, String::class.java)
            .observeStickyForever(viewModel.stickyForeverObserver)
    }
}
