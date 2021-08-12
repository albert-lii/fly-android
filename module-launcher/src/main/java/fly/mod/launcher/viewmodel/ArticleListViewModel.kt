package fly.mod.launcher.viewmodel

import android.os.Environment
import androidx.lifecycle.MutableLiveData

import com.google.gson.reflect.TypeToken
import fly.mod.launcher.data.Article
import fly.mod.launcher.data.CommonListDto
import fly.mod.lib.common.api.ApiClient
import fly.mod.lib.common.base.BaseAppViewModel
import org.fly.base.utils.AppUtils
import org.fly.http.response.InfoResponse
import java.io.File


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
            // 使用Flow进行请求
//            ApiClient.getInstance().requestFlow<CommonListDto<Article>>(object :
//                TypeToken<InfoResponse<CommonListDto<Article>>>() {}.type) {
//                it.get(url = "blog/article", mapOf(), mapOf())
//            }.collect {
//                it.onSuccess {
//                    articleList.value = it!!.results
//                }.onFailure { code, msg ->
//                    showToast(msg ?: "获取文章列表失败")
//                }.onCatch { error ->
//                    showToast(error.errorMsg)
//                }
//            }

            // 使用普通协程进行请求
            ApiClient.getInstance()
                .get<CommonListDto<Article>>(
                    url = "blog/article",
                    type = object : TypeToken<InfoResponse<CommonListDto<Article>>>() {}.type
                )
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