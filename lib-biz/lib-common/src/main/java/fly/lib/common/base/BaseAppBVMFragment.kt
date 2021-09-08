package fly.lib.common.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import fly.lib.common.dialog.LoadingDialog
import org.fly.base.arch.mvvm.BaseBVMFragment
import org.fly.base.arch.mvvm.BaseViewModel

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/9/3 4:18 下午
 * @description: -
 * @since: 1.0.0
 */
abstract class BaseAppBVMFragment<B : ViewDataBinding, VM : BaseViewModel> :
    BaseBVMFragment<B, VM>() {
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