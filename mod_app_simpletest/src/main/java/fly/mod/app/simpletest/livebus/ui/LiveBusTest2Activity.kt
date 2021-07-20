package fly.mod.app.simpletest.livebus.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import fly.mod.app.simpletest.livebus.Constants
import fly.mod.app.simpletest.R
import fly.mod.app.simpletest.databinding.StActivityLivebusTest2Binding
import fly.mod.app.simpletest.livebus.viewmodel.LiveBusTest2ViewModel
import fly.mod.lib.common.base.BaseAppBVMActivity
import fly.mod.lib.common.router.RouteConstants
import org.fly.base.extensions.singleClick
import org.fly.base.communication.livebus.LiveBus


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
