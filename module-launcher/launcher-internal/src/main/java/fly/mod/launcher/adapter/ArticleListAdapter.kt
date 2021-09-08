package fly.mod.launcher.adapter

import fly.mod.launcher.R
import fly.mod.launcher.dto.ArticleDto
import fly.mod.launcher.databinding.LauncherRecyclerItemArticleBinding
import org.fly.uikit.recyclerview.BaseBindRecyclerAdapter

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/3/24 1:36 PM
 * @description: 文章列表适配器
 * @since: 1.0.0
 */
class ArticleListAdapter : BaseBindRecyclerAdapter<LauncherRecyclerItemArticleBinding, ArticleDto>() {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.launcher_recycler_item_article
    }

    override fun onBindingItem(
        binding: LauncherRecyclerItemArticleBinding,
        item: ArticleDto,
        position: Int
    ) {
        binding.tvTitle.text = item.title
    }
}