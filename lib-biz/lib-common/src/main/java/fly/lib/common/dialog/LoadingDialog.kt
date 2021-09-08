package fly.lib.common.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import fly.lib.common.R
import fly.lib.common.databinding.CommonDialogLoadingBinding
import org.fly.uikit.dialog.BaseBindDialogFragment

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/9/6 7:29 下午
 * @description: 加载弹框
 * @since: 1.0.0
 */
class LoadingDialog : BaseBindDialogFragment<CommonDialogLoadingBinding>() {
    private var cancelAction: (() -> Unit)? = null

    override fun getDialogStyle(): Int {
        return STYLE_NO_FRAME
    }

    override fun getDialogTheme(): Int {
        return R.style.fly_uikit_TransparentDialog
    }

    override fun getLayoutId(): Int {
        return R.layout.common_dialog_loading
    }

    override fun initialize(view: View, savedInstanceState: Bundle?) {
//        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setOnCancelListener { cancelAction?.invoke() }
    }

    override fun show(activity: FragmentActivity, tag: String?) {
        super.show(activity, tag)
        binding.loadingView.start()
    }

    override fun show(fragment: Fragment, tag: String?) {
        super.show(fragment, tag)
        binding.loadingView.start()
    }

    override fun dismiss() {
        binding.loadingView.stop()
        super.dismiss()
    }

    override fun dismissAllowingStateLoss() {
        binding.loadingView.stop()
        super.dismissAllowingStateLoss()
    }

    fun setCancelAction(action: (() -> Unit)? = null) {
        this.cancelAction = action
    }
}