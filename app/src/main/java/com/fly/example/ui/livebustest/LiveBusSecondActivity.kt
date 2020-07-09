package com.fly.example.ui.livebustest

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.fly.example.Constants
import com.fly.example.R
import com.fly.example.base.BaseAppBVMActivity
import com.fly.example.databinding.ActivityLivebusSecondBinding
import com.fly.example.viewmodel.LiveBusSecondViewModel
import kotlinx.android.synthetic.main.activity_livebus_second.*
import org.we.fly.extensions.singleClick
import org.we.fly.utils.livebus.LiveBus


/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/3 2:29 PM
 * @description: LiveBus测试页面2
 * @since: 1.0.0
 */
class LiveBusSecondActivity :
    BaseAppBVMActivity<ActivityLivebusSecondBinding, LiveBusSecondViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_livebus_second
    }

    override fun createViewModel(): LiveBusSecondViewModel {
        return LiveBusSecondViewModel()
    }

    override fun initialize(savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        binding.titlebar.setLeftClick(View.OnClickListener{
           finish()
        })
        addListener()
        initObserve()
    }

    private fun addListener() {
        btn_post_lifecycle.singleClick {
            viewModel.postTestLifecycleMsg()
        }
        btn_post_end.singleClick {
            viewModel.postTestMsg()
        }
    }

    private fun initObserve() {
        LiveBus.get(Constants.BK_FIRST_POST_TEST, String::class.java)
            .observe(this, object : Observer<String> {
                override fun onChanged(t: String?) {
                    viewModel.normalObserveData.value = "普通订阅：${t}"
                }

            })
        LiveBus.get(Constants.BK_FIRST_POST_TEST, String::class.java)
            .observeSticky(this, object : Observer<String> {
                override fun onChanged(t: String?) {
                    viewModel.stickyObserveData.value = "Sticky订阅：${t}"
                }
            })
        LiveBus.get(Constants.BK_FIRST_POST_TEST, String::class.java)
            .observeForever(viewModel.foreverObserver)
        LiveBus.get(Constants.BK_FIRST_POST_TEST, String::class.java)
            .observeStickyForever(viewModel.stickyForeverObserver)
    }
}
