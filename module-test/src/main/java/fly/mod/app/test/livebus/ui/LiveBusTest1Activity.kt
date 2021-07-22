package fly.mod.app.test.livebus.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import fly.mod.app.test.R
import fly.mod.app.test.databinding.StActivityLivebusTest1Binding
import fly.mod.app.test.livebus.Constants
import fly.mod.app.test.livebus.viewmodel.LiveBusTest1ViewModel
import fly.mod.lib.common.base.BaseAppBVMActivity
import fly.mod.lib.common.router.RouteConstants
import fly.mod.lib.common.router.RouterUtils
import org.fly.base.communication.livebus.LiveBus


/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/3 2:29 PM
 * @description: LiveBus测试页面1
 * @since: 1.0.0
 */
@Route(path = RouteConstants.PAGE_ST_LIVEBUS_TEST1)
class LiveBusTest1Activity :
    BaseAppBVMActivity<StActivityLivebusTest1Binding, LiveBusTest1ViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.st_activity_livebus_test1
    }

    override fun createViewModel(): LiveBusTest1ViewModel {
        return LiveBusTest1ViewModel()
    }

    override fun initialize(savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        addListener()
        addObserver()
    }

    private fun addListener() {
        binding.btnPost.singleClick {
            viewModel.postMsgToTest2()
            RouterUtils.getInstance()
                .navigate(this@LiveBusTest1Activity, RouteConstants.PAGE_ST_LIVEBUS_TEST2)
        }
    }

    private fun addObserver() {
        LiveBus.get(Constants.LK_TEST2_POST, String::class.java)
            .observe(this, object : Observer<String> {
                override fun onChanged(t: String?) {
                    viewModel.normalObserveData.value = "普通订阅：${t}"

                }
            })
        LiveBus.get(Constants.LK_TEST2_POST, String::class.java)
            .observeSticky(this, object : Observer<String> {
                override fun onChanged(t: String?) {
                    viewModel.stickyObserveData.value = "Sticky订阅：${t}"
                }
            })
        LiveBus.get(Constants.LK_TEST2_POST, String::class.java)
            .observeForever(viewModel.foreverObserver)
        LiveBus.get(Constants.LK_TEST2_POST, String::class.java)
            .observeStickyForever(viewModel.stickyForeverObserver)
    }
}
