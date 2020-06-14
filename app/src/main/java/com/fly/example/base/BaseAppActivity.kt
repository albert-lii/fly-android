package com.fly.example.base

import android.content.Intent
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import org.we.fly.base.mvvm.BaseBindingActivity
import org.we.fly.base.mvvm.BaseViewModel

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/12 7:37 PM
 * @description: --
 * @since: 1.0.0
 */
abstract class BaseAppActivity<B : ViewDataBinding> : BaseBindingActivity<B>() {

    override fun showLoadingUI(isShow: Boolean) {

    }

    override fun showEmptyUI(isShow: Boolean) {

    }

    override fun showToast(str: String, duration: Int) {
        Toast.makeText(applicationContext, str, duration).show()
    }

    override fun showToast(strId: Int, duration: Int) {
        Toast.makeText(applicationContext, getString(strId), duration).show()
    }

    override fun navigateTo(page: Any) {
        startActivity(Intent(this, page as Class<*>))
    }

    override fun backPress(arg: Any?) {
        onBackPressed()
    }

    override fun finishPage(arg: Any?) {
        finish()
    }
}