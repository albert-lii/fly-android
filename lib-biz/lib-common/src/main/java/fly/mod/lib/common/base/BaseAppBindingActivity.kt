package fly.mod.lib.common.base

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.databinding.ViewDataBinding
import org.fly.base.arch.mvvm.BaseBindingActivity
import org.fly.base.arch.FlyArchCsts
import org.fly.base.utils.SystemUIUtils

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
        SystemUIUtils.transparentStatusBar(this)
    }

    override fun showLoadingView(isShow: Boolean) {

    }

    override fun showEmptyView(isShow: Boolean) {

    }


    override fun navigate(page: Any) {
        startActivity(Intent(this, page as Class<*>))
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

    protected fun showToast(str: String) {
        showToast(str, null)
    }

    protected fun showToast(str: String, duration: Int?) {
        val map = HashMap<String, Any>().apply {
            put(
                FlyArchCsts.FLY_TOAST_KEY_CTYPE,
                FlyArchCsts.FLY_TOAST_VAL_CTYPE_STR
            )
            put(FlyArchCsts.FLY_TOAST_KEY_CONTENT, str)
            if (duration != null) {
                put(FlyArchCsts.FLY_TOAST_KEY_DURATION, duration)
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
                FlyArchCsts.FLY_TOAST_KEY_CTYPE,
                FlyArchCsts.FLY_TOAST_VAL_CTYPE_RESID
            )
            put(FlyArchCsts.FLY_TOAST_KEY_CONTENT, resId)
            if (duration != null) {
                put(FlyArchCsts.FLY_TOAST_KEY_DURATION, duration)
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