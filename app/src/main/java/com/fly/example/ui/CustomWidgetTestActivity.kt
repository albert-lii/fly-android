package com.fly.example.ui

import android.os.Bundle
import android.view.View
import com.fly.example.R
import com.fly.example.base.BaseAppBindingActivity
import com.fly.example.databinding.ActivityCustomWidgetTestBinding

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/7/9 10:14 AM
 * @description: 自定义控件测试页面
 * @since: 1.0.0
 */
class CustomWidgetTestActivity : BaseAppBindingActivity<ActivityCustomWidgetTestBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_custom_widget_test
    }

    override fun init(savedInstanceState: Bundle?) {
        binding.titlebar.setLeftClick(View.OnClickListener {
            finish()
        })
    }
}