package fly.mod.launcher.repo

import androidx.annotation.IntRange
import fly.lib.common.dto.CommonListDto
import fly.mod.launcher.dto.ArticleDto
import org.fly.http.response.ResponseHolder

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/14 4:57 下午
 * @description: -
 * @since: 1.0.0
 */
interface IndexRepo {
    /**
     * 获取文章列表
     */
    suspend fun getArticleList(
        @IntRange(from = 1) page_no: Int = 1,
        page_size: Int = 20
    ): ResponseHolder<CommonListDto<ArticleDto>>
}