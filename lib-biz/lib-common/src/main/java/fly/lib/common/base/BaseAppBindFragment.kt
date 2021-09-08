package fly.lib.common.base

import androidx.databinding.ViewDataBinding
import fly.lib.common.dialog.LoadingDialog
import org.fly.base.arch.mvvm.BaseBindFragment

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/9/3 6:19 下午
 * @description: -
 * @since: 1.0.0
 */
abstract class BaseAppBindFragment<B : ViewDataBinding> : BaseBindFragment<B>() {
    private val loadingDialog by lazy { LoadingDialog() }

    override fun showLoadingView(isShow: Boolean) {
        try {
            if (isShow) {
                if (isVisible && !loadingDialog.isAdded) {
                    loadingDialog.show(this)
                }
            } else {
                if (isVisible) {
                    loadingDialog.dismissAllowingStateLoss()
                }
            }
        } catch (e: Exception) {
        }
    }

    override fun showEmptyView(isShow: Boolean) {

    }
}