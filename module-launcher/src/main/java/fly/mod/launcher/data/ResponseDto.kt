package fly.mod.launcher.data

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/3/22 7:32 PM
 * @description: -
 * @since: 1.0.0
 */

/**
 * 通用的List数据模型
 */
data class CommonListDto<T>(
    var count: Int,
    var results: ArrayList<T>
)

/**
 * 文章信息
 */
data class Article(
    var id: Int,
    var title: String // 文章标题
)