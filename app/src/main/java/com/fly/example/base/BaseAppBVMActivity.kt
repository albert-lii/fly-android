package com.fly.example.base

import android.content.Intent
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import org.we.fly.base.mvvm.BaseBVMActivity
import org.we.fly.base.mvvm.BaseViewModel

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/11 7:47 PM
 * @description: --
 * @since: 1.0.0
 */
abstract class BaseAppBVMActivity<B : ViewDataBinding, VM : BaseViewModel> :
    BaseBVMActivity<B, VM>() {

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