package fly.mod.launcher.repo

import com.google.gson.reflect.TypeToken
import fly.lib.common.dto.CommonListDto
import fly.lib.common.http.FlyHttp
import fly.mod.launcher.dto.ArticleDto
import org.fly.http.response.InfoResponse
import org.fly.http.response.ResponseHolder

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/21 5:37 下午
 * @description: -
 * @since: 1.0.0
 */
class IndexRepoImpl : IndexRepo {
    private val URL_ARTICLE_LIST = "v1/blog/article" // 获取文章列表

    override suspend fun getArticleList(
        pageNo: Int,
        pageSize: Int
    ): ResponseHolder<CommonListDto<ArticleDto>> {
        return FlyHttp.getInstance()
            .get(
                url = URL_ARTICLE_LIST,
                params = mapOf<String, String>(
                    "pagez_no" to pageNo.toString(),
                    "page_size" to pageSize.toString()
                ),
                type = object : TypeToken<InfoResponse<CommonListDto<ArticleDto>>>() {}.type,
            )
    }
}