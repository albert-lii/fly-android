package fly.lib.common.dto

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/9/5 2:44 下午
 * @description: 通用的List数据模型
 * @since: 1.0.0
 */

data class CommonListDto<T>(
    var count: Int,
    var results: ArrayList<T>
)
