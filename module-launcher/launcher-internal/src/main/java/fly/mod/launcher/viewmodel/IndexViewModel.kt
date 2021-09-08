package fly.mod.launcher.viewmodel

import androidx.lifecycle.MutableLiveData
import fly.lib.common.base.BaseAppViewModel
import fly.mod.launcher.dto.ArticleDto
import fly.mod.launcher.repo.IndexRepoImpl

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/14 5:24 下午
 * @description: -
 * @since: 1.0.0
 */
class IndexViewModel : BaseAppViewModel() {
    val articleList = MutableLiveData<ArrayList<ArticleDto>>()
    private val repo by lazy { IndexRepoImpl() }

    /**
     * 获取文章列表
     */
    fun getArticleList() {
        launchOnUI {
            showLoadingView(true)
            repo.getArticleList(1, 20)
                .onCompletion { showLoadingView(false) }
                .onSuccess {
                    articleList.value = it!!.results
                }
                .onFailure { code, msg ->
                    showToast(msg ?: "获取文章列表失败")
                }
                .onCatch { error ->
                    showToast(error.errorMsg)
                }
        }
    }
}