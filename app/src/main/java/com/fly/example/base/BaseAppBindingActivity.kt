package com.fly.example.base

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.databinding.ViewDataBinding
import com.blankj.utilcode.util.BarUtils
import org.we.fly.base.FlyBaseConstants
import org.we.fly.base.arch.BaseBindingActivity

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/12 7:37 PM
 * @description: --
 * @since: 1.0.0
 */
abstract class BaseAppBindingActivity<B : ViewDataBinding> : BaseBindingActivity<B>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarUtils.setStatusBarLightMode(this, true)
        BarUtils.transparentStatusBar(this)
    }

    override fun showLoadingUI(isShow: Boolean) {

    }

    override fun showEmptyUI(isShow: Boolean) {

    }


    override fun navigateTo(page: Any) {
        startActivity(Intent(this, page as Class<*>))
    }

    override fun showToast(map: Map<String, *>) {
        if (map[FlyBaseConstants.FLY_TOAST_KEY_CONTENT_TYPE] == FlyBaseConstants.FLY_TOAST_CONTENT_TYPE_STR) {
            if (map[FlyBaseConstants.FLY_TOAST_KEY_DURATION] == null) {
                Toast.makeText(
                    this,
                    map[FlyBaseConstants.FLY_TOAST_KEY_CONTENT] as String,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    map[FlyBaseConstants.FLY_TOAST_KEY_CONTENT] as String,
                    map[FlyBaseConstants.FLY_TOAST_KEY_DURATION] as Int
                ).show()
            }
        } else if (map[FlyBaseConstants.FLY_TOAST_KEY_CONTENT_TYPE] == FlyBaseConstants.FLY_TOAST_CONTENT_TYPE_RESID) {
            if (map[FlyBaseConstants.FLY_TOAST_KEY_DURATION] == null) {
                Toast.makeText(
                    this,
                    map[FlyBaseConstants.FLY_TOAST_KEY_CONTENT] as Int,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    map[FlyBaseConstants.FLY_TOAST_KEY_CONTENT] as Int,
                    map[FlyBaseConstants.FLY_TOAST_KEY_DURATION] as Int
                ).show()
            }
        }
    }

    protected fun showToast(str: String) {
        showToast(str, null)
    }

    protected fun showToast(str: String, duration: Int?) {
        val map = HashMap<String, Any>().apply {
            put(
                FlyBaseConstants.FLY_TOAST_KEY_CONTENT_TYPE,
                FlyBaseConstants.FLY_TOAST_CONTENT_TYPE_STR
            )
            put(FlyBaseConstants.FLY_TOAST_KEY_CONTENT, str)
            if (duration != null) {
                put(FlyBaseConstants.FLY_TOAST_KEY_DURATION, duration)
            }
        }
        showToast(map)
    }

    protected fun showToast(@StringRes resId: Int) {
        showToast(resId, null)
    }

    protected fun showToast(@StringRes resId: Int, duration: Int?) {
        val map = HashMap<String, Any>().apply {
            put(
                FlyBaseConstants.FLY_TOAST_KEY_CONTENT_TYPE,
                FlyBaseConstants.FLY_TOAST_CONTENT_TYPE_RESID
            )
            put(FlyBaseConstants.FLY_TOAST_KEY_CONTENT, resId)
            if (duration != null) {
                put(FlyBaseConstants.FLY_TOAST_KEY_DURATION, duration)
            }
        }
        showToast(map)
    }

    override fun backPress(arg: Any?) {
        onBackPressed()
    }

    override fun finishPage(arg: Any?) {
        finish()
    }
}