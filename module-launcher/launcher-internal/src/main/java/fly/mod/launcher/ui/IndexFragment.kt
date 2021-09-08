package fly.mod.launcher.ui

import android.os.Bundle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import fly.lib.common.EMPTY_EVENT_VALUE
import fly.lib.common.base.BaseAppBVMFragment
import fly.mod.launcher.R
import fly.mod.launcher.adapter.ArticleListAdapter
import fly.mod.launcher.adapter.HeaderAdapter
import fly.mod.launcher.databinding.LauncherFragmentIndexBinding
import fly.mod.launcher.event.LAUNCHER_EVENT_OPEN_SIDEBAR
import fly.mod.launcher.viewmodel.IndexViewModel
import org.fly.base.exts.observeNonNull
import org.fly.base.exts.singleClick
import org.fly.base.signal.livebus.LiveBus

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/14 4:57 下午
 * @description: 首页
 * @since: 1.0.0
 */
class IndexFragment : BaseAppBVMFragment<LauncherFragmentIndexBinding, IndexViewModel>() {
    private var headerAdapter: HeaderAdapter? = null
    private val articleAdapter by lazy { ArticleListAdapter() }
    lateinit var adapter: ConcatAdapter

    override fun getLayoutId(): Int {
        return R.layout.launcher_fragment_index
    }

    override fun createViewModel(): IndexViewModel {
        return IndexViewModel()
    }

    override fun initialize(savedInstanceState: Bundle?) {
        initView()
        addObserver()
        addListener()
        viewModel.getArticleList()
    }

    private fun initView() {
        headerAdapter = HeaderAdapter(this)
        adapter = ConcatAdapter(headerAdapter, articleAdapter)
        binding.rvIndex.layoutManager = LinearLayoutManager(context)
        binding.rvIndex.adapter = adapter
    }

    private fun addObserver() {
        viewModel.articleList.observeNonNull(this) {
            articleAdapter.refreshItems(it)
        }
    }

    private fun addListener() {
        binding.ltvMenu.singleClick {
            LiveBus.get(LAUNCHER_EVENT_OPEN_SIDEBAR).post(EMPTY_EVENT_VALUE)
        }
    }
}