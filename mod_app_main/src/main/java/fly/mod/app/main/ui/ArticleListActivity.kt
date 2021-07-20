package fly.mod.app.main.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import fly.mod.app.main.R
import fly.mod.app.main.adapter.ArticleListAdapter
import fly.mod.app.main.data.Article
import fly.mod.app.main.databinding.MActivityArticleListBinding
import fly.mod.app.main.viewmodel.ArticleListViewModel
import fly.mod.lib.common.base.BaseAppBVMActivity
import fly.mod.lib.common.router.RouteConstants
import org.fly.base.arch.recyclerview.BaseRecyclerAdapter
import org.fly.base.extensions.observeNonNull

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/3/22 5:45 PM
 * @description: 文章列表页
 * @since: 1.0.0
 */
@Route(path = RouteConstants.PAGE_M_ARTICLE_LIST)
class ArticleListActivity :
    BaseAppBVMActivity<MActivityArticleListBinding, ArticleListViewModel>() {

    private var adapter = ArticleListAdapter()

    override fun getLayoutId(): Int {
        return R.layout.m_activity_article_list
    }

    override fun createViewModel(): ArticleListViewModel {
        return ArticleListViewModel()
    }

    override fun initialize(savedInstanceState: Bundle?) {
        initUI()
        addObserver()
        addListener()
    }

    private fun initUI() {
        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.rvList.adapter = adapter
    }

    private fun addObserver() {
        viewModel.articleList.observeNonNull(this) {
            adapter.refreshItems(it)
        }
    }

    private fun addListener() {
        adapter.setOnItemClickListener(object : BaseRecyclerAdapter.OnItemClickListener<Article> {
            override fun onItemClick(holder: Any, item: Article, position: Int) {
                val intent = Intent()
                intent.setAction(Intent.ACTION_VIEW)
                intent.setData(Uri.parse(item.link))
                startActivity(intent)
            }
        })
    }
}