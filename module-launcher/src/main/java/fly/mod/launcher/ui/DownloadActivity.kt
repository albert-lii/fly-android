package fly.mod.launcher.ui

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import fly.mod.launcher.R
import fly.mod.launcher.adapter.ArticleListAdapter
import fly.mod.launcher.data.Article
import fly.mod.launcher.databinding.MActivityDownloadBinding
import fly.mod.launcher.viewmodel.ArticleListViewModel
import fly.mod.launcher.viewmodel.DownloadViewModel
import fly.mod.lib.common.base.BaseAppBVMActivity
import org.fly.base.arch.recyclerview.BaseRecyclerAdapter
import org.fly.base.extensions.observeNonNull
import org.fly.base.extensions.singleClick

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
        }
        binding.btnCancel.singleClick {
            viewModel.cancel()
        }
    }
}