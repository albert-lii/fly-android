package fly.mod.lib.common.base

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.databinding.ViewDataBinding
import org.fly.base.arch.mvvm.BaseBVMActivity
import org.fly.base.arch.mvvm.BaseViewModel
import org.fly.base.arch.FlyArchCsts
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemUIUtils.transparentStatusBar(this)
    }

    override fun showLoadingView(isShow: Boolean) {

    }

    override fun showEmptyView(isShow: Boolean) {

    }
}