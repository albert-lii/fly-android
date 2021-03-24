package fly.mod.app.main.api

import fly.mod.app.main.data.Article
import fly.mod.app.main.data.CommonListDto
import fly.mod.lib.common.api.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/3/22 5:32 PM
 * @description: -
 * @since: 1.0.0
 */
interface FlyInterface {
    /**
     * 获取文章列表
     */
    @GET("article/")
    suspend fun get_article_list(@Query("page_size") size: Int): ApiResponse<CommonListDto<Article>>
}