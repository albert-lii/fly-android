package fly.lib.common.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import fly.lib.common.dialog.LoadingDialog
import org.fly.base.arch.mvvm.BaseBVMActivity
import org.fly.base.arch.mvvm.BaseViewModel
import org.fly.base.utils.SystemUIUtils


/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/11 7:47 PM
 * @description: --
 * @since: 1.0.0
 */
abstract class BaseAppBVMActivity<B : ViewDataBinding, VM : BaseViewModel> :
    BaseBVMActivity<B, VM>() {
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