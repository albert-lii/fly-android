package org.fly.base.arch.mvvm

import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import org.fly.base.utils.ToastUtils

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/20 12:08 PM
 * @description: Activity的基类
 * @since: 1.0.0
 */
abstract class BaseActivity : AppCompatActivity(), ViewBehavior {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initContentView()
        initialize(savedInstanceState)
    }

    protected open fun initContentView() {
        setContentView(getLayoutId())
    }

    protected abstract @LayoutRes
    fun getLayoutId(): Int

    /**
     *  初始化操作
     */
    protected abstract fun initialize(savedInstanceState: Bundle?)

    protected fun showToast(text: String, showLong: Boolean = false) {
        showToast(ToastEvent(content = text, showLong = showLong))
    }

    protected fun showToast(@StringRes resId: Int, showLong: Boolean = false) {
        showToast(ToastEvent(contentResId = resId, showLong = showLong))
    }

    override fun showToast(event: ToastEvent) {
        if (event.content != null) {
            ToastUtils.showToast(this, event.content!!, event.showLong)
        } else if (event.contentResId != null) {
            ToastUtils.showToast(this, getString(event.contentResId!!), event.showLong)
        }
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