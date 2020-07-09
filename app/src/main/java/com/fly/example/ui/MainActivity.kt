package com.fly.example.ui

import android.os.Bundle
import com.fly.example.R
import com.fly.example.base.BaseAppBindingActivity
import com.fly.example.databinding.ActivityMainBinding
import com.fly.example.ui.livebustest.LiveBusFirstActivity
import org.we.fly.extensions.singleClick

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/3 2:29 PM
 * @description: 主页面
 * @since: 1.0.0
 */
class MainActivity : BaseAppBindingActivity<ActivityMainBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun init(savedInstanceState: Bundle?) {
        binding.btnLivebus.singleClick {
            navigateTo(LiveBusFirstActivity::class.java)
        }
        binding.btnCustomWidgetTest.singleClick {
            navigateTo(CustomWidgetTestActivity::class.java)
        }
    }
}
