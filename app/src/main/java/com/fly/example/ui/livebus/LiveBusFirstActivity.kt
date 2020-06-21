package com.fly.example.ui.livebus

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.fly.example.BR
import com.fly.example.Constants
import com.fly.example.R
import com.fly.example.base.BaseAppBVMActivity
import com.fly.example.databinding.ActivityLivebusFirstBinding
import com.fly.example.viewmodel.LiveBusFirstViewModel
import kotlinx.android.synthetic.main.activity_livebus_first.*
import org.we.fly.extensions.singleClick
import org.we.fly.utils.livebus.LiveBus


/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/3 2:29 PM
 * @description: LiveBus测试页面1
 * @since: 1.0.0
 */
class LiveBusFirstActivity :
    BaseAppBVMActivity<ActivityLivebusFirstBinding, LiveBusFirstViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_livebus_first
    }

    override fun createViewModel(): LiveBusFirstViewModel {
        return LiveBusFirstViewModel()
    }

    override fun initialize(savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        binding.titlebar.leftClick(View.OnClickListener {
            finish()
        })
        addListener()
        initObserve()
    }

    private fun addListener() {
        btn_post.singleClick {
            viewModel.postTestMsg()
            viewModel.changeReceiveCountUI()
        }
    }

    private fun initObserve() {
        LiveBus.get(Constants.BK_SECOND_POST_CREATED_TEST, String::class.java)
            .observerAlwaysBeActive(true)
            .observe(this, object : Observer<String> {
                override fun onChanged(t: String?) {
                    viewModel.recevieCountOnCREATED++;
                    viewModel.changeReceiveCountUI()
                }
            })
        LiveBus.get(Constants.BK_SECOND_POST_STARTED_TEST, String::class.java)
            .observerAlwaysBeActive(false)
            .observe(this, object : Observer<String> {
                override fun onChanged(t: String?) {
                    viewModel.recevieCountOnSTARTED++;
                    viewModel.changeReceiveCountUI()
                }
            })
        LiveBus.get(Constants.BK_SECOND_POST_TEST, String::class.java)
            .observe(this, object : Observer<String> {
                override fun onChanged(t: String?) {
                    viewModel.normalObserveData.value = "普通订阅：${t}"
                }
            })
        LiveBus.get(Constants.BK_SECOND_POST_TEST, String::class.java)
            .observeSticky(this, object : Observer<String> {
                override fun onChanged(t: String?) {
                    viewModel.stickyObserveData.value = "Sticky订阅：${t}"
                }
            })
        LiveBus.get(Constants.BK_SECOND_POST_TEST, String::class.java)
            .observeForever(viewModel.foreverObserver)
        LiveBus.get(Constants.BK_SECOND_POST_TEST, String::class.java)
            .observeStickyForever(viewModel.stickyForeverObserver)
    }
}
