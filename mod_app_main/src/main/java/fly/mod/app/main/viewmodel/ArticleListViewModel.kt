package fly.mod.app.main.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import fly.mod.app.main.api.FlyInterface
import fly.mod.app.main.data.Article
import fly.mod.lib.common.api.ApiClient
import fly.mod.lib.common.base.BaseAppViewModel


/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/3/22 5:48 PM
 * @description: -
 * @since: 1.0.0
 */
class ArticleListViewModel : BaseAppViewModel() {

    val articleList = MutableLiveData<ArrayList<Article>>()

    init {
        get_article_list()
    }

    /**
     * 获取文章列表
     */
    fun get_article_list() {
        launchOnUI {
            var isFirstSuccess = false
            ApiClient.getInstance()
                .requestSafely(FlyInterface::class.java) {
                    it.get_article_list(20)
                }.doSuccess {
                    articleList.value = it!!.results
                    Log.e("XXX", ">>>>>>>第一次完毕")
                    isFirstSuccess = true
                }
                .doFailure { code, msg -> showToast(msg ?: "获取文章列表失败") }
                .doError { ex, error -> showToast(error.message) }
                .procceed()
            if (isFirstSuccess) {
                Log.e("XXX", ">>>>>>>第二次请求")
                ApiClient.getInstance()
                    .requestSafely(FlyInterface::class.java) {
                        it.get_article_list(20)
                    }.doSuccess {
                        articleList.value = it!!.results
                    }
                    .doFailure { code, msg -> showToast(msg ?: "获取文章列表失败") }
                    .doError { ex, error -> showToast(error.message) }
                    .procceed()
            }
        }
    }
}