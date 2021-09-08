package fly.mod.launcher.ui

import android.os.Bundle
import fly.mod.launcher.R
import fly.mod.launcher.databinding.MActivityDownloadBinding
import fly.mod.launcher.viewmodel.DownloadViewModel
import fly.lib.common.base.BaseAppBVMActivity
import org.fly.base.exts.observeNonNull
import org.fly.base.exts.singleClick

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/12 5:46 下午
 * @description: -
 * @since: 1.0.0
 */
class DownloadActivity : BaseAppBVMActivity<MActivityDownloadBinding, DownloadViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.m_activity_download
    }

    override fun createViewModel(): DownloadViewModel {
        return DownloadViewModel()
    }

    override fun initialize(savedInstanceState: Bundle?) {
        addObserver()
        addListener()
    }

    private fun addObserver() {
        viewModel.progressLD.observeNonNull(this) {
            binding.tvProgress.text = it
        }
    }

    private fun addListener() {
        binding.btnStart.singleClick {
            viewModel.download()
//            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                ToastUtils.showToast(this,"有外部存储")
//            } else {
//                ToastUtils.showToast(this,"没有外部存储")
//            }
        }
        binding.btnCancel.singleClick {
            viewModel.cancel()
        }
    }
}