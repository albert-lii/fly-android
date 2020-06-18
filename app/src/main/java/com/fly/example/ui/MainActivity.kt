package com.fly.example.ui

import android.os.Bundle
import com.fly.example.R
import com.fly.example.base.BaseAppActivity
import com.fly.example.databinding.ActivityMainBinding
import com.fly.example.ui.livebus.LiveBusFirstActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.we.fly.extensions.singleClick

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/3 2:29 PM
 * @description: 主页面
 * @since: 1.0.0
 */
class MainActivity : BaseAppActivity<ActivityMainBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun init(savedInstanceState: Bundle?) {
        btn_livebus.singleClick {
            navigateTo(LiveBusFirstActivity::class.java)
        }
    }
}
