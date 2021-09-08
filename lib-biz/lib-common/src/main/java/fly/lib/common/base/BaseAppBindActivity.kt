package fly.lib.common.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import fly.lib.common.dialog.LoadingDialog
import org.fly.base.arch.mvvm.BaseBindActivity
import org.fly.base.utils.SystemUIUtils

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/12 7:37 PM
 * @description: --
 * @since: 1.0.0
 */
abstract class BaseAppBindActivity<B : ViewDataBinding> : BaseBindActivity<B>() {
    private val loadingDialog by lazy { LoadingDialog() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemUIUtils.transparentStatusBar(this)
    }

    override fun showLoadingView(isShow: Boolean) {
        try {
            if (isShow) {
                if (!isFinishing && !loadingDialog.isAdded) {
                    loadingDialog.show(this)
                }
            } else {
                if (!isFinishing) {

                    loadingDialog.dismissAllowingStateLoss()
                }
            }
        } catch (e: Exception) {
        }
    }

    override fun showEmptyView(isShow: Boolean) {

    }
}