package org.fly.base.arch.mvvm

import android.R
import android.content.Intent
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import org.fly.base.arch.FlyArchCsts


/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/12 10:06 AM
 * @description: 集成DataBinding的Activity的基类
 * @since: 1.0.0
 */
abstract class BaseBindingActivity<B : ViewDataBinding> : BaseActivity(),
    ViewBehavior {

    protected lateinit var binding: B
        private set

    override fun initContentView() {
        injectDataBinding()
    }

    protected fun injectDataBinding() {
        binding = DataBindingUtil.setContentView(this,getLayoutId())
        binding.lifecycleOwner = this
    }

    override fun onDestroy() {
        binding.unbind()
        super.onDestroy()
    }

    override fun showToast(map: Map<String, *>) {
        if (map[FlyArchCsts.FLY_TOAST_KEY_CTYPE] == FlyArchCsts.FLY_TOAST_VAL_CTYPE_STR) {
            if (map[FlyArchCsts.FLY_TOAST_KEY_DURATION] == null) {
                Toast.makeText(
                    this,
                    map[FlyArchCsts.FLY_TOAST_KEY_CONTENT] as String,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    map[FlyArchCsts.FLY_TOAST_KEY_CONTENT] as String,
                    map[FlyArchCsts.FLY_TOAST_KEY_DURATION] as Int
                ).show()
            }
        } else if (map[FlyArchCsts.FLY_TOAST_KEY_CTYPE] == FlyArchCsts.FLY_TOAST_VAL_CTYPE_RESID) {
            if (map[FlyArchCsts.FLY_TOAST_KEY_DURATION] == null) {
                Toast.makeText(
                    this,
                    map[FlyArchCsts.FLY_TOAST_KEY_CONTENT] as Int,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    map[FlyArchCsts.FLY_TOAST_KEY_CONTENT] as Int,
                    map[FlyArchCsts.FLY_TOAST_KEY_DURATION] as Int
                ).show()
            }
        }
    }

    protected fun showToast(str: String, duration: Int = Toast.LENGTH_SHORT) {
        val map = HashMap<String, Any>().apply {
            put(
                FlyArchCsts.FLY_TOAST_KEY_CTYPE,
                FlyArchCsts.FLY_TOAST_VAL_CTYPE_STR
            )
            put(FlyArchCsts.FLY_TOAST_KEY_CONTENT, str)
            put(FlyArchCsts.FLY_TOAST_KEY_DURATION, duration)
        }
        showToast(map)
    }

    protected fun showToast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) {
        val map = HashMap<String, Any>().apply {
            put(
                FlyArchCsts.FLY_TOAST_KEY_CTYPE,
                FlyArchCsts.FLY_TOAST_VAL_CTYPE_RESID
            )
            put(FlyArchCsts.FLY_TOAST_KEY_CONTENT, resId)
            put(FlyArchCsts.FLY_TOAST_KEY_DURATION, duration)
        }
        showToast(map)
    }


    override fun navigate(page: Any) {
        startActivity(Intent(this, page as Class<*>))
    }

    override fun backPress(arg: Any?) {
        onBackPressed()
    }

    override fun finishPage(arg: Any?) {
        finish()
    }
}