package fly.mod.app.startup.adapter

import fly.mod.app.startup.R
import fly.mod.app.startup.data.Article
import fly.mod.app.startup.databinding.MRecyclerItemArticleBinding
import org.fly.base.arch.recyclerview.BaseBindingRecyclerAdapter

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/3/24 1:36 PM
 * @description: -
 * @since: 1.0.0
 */
class ArticleListAdapter : BaseBindingRecyclerAdapter<MRecyclerItemArticleBinding, Article>() {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.m_recycler_item_article
    }

    override fun onBindItem(binding: MRecyclerItemArticleBinding?, item: Article, position: Int) {
        binding?.tvTitle?.text = item.title
    }
}