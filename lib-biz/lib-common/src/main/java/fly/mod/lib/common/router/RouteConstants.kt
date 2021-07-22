package fly.mod.lib.common.router

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/3/15 5:11 PM
 * @description: 路由路径，路径不能使用一级路径，如："/app"，最起码要使用二级路径，如："/app/main"，否报错
 * @since: 1.0.0
 */
object RouteConstants {
    // 首页
    const val PAGE_M_MAIN: String = "/app/main"

    // 文章列表页
    const val PAGE_M_ARTICLE_LIST: String = "/app/articleList"

    // 测试首页
    const val PAGE_ST_TEST_HOME: String = "/simpleTest/testHome"

    // 简单的协程测试页面
    const val PAGE_ST_COROUTINE: String = "/simpleTest/coroutine/home"

    // 协程的简单测试（一）
    const val PAGE_ST_COROUTINE_TEST1: String = "/simpleTest/coroutine/test1"

    // 协程的简单测试（二）
    const val PAGE_ST_COROUTINE_TEST2: String = "/simpleTest/coroutine/test2"

    // LiveBus测试（一）
    const val PAGE_ST_LIVEBUS_TEST1: String = "/simpleTest/livebus/test1"

    // LiveBus测试（二）
    const val PAGE_ST_LIVEBUS_TEST2: String = "/simpleTest/livebus/test2"

    // 曲线图测试
    const val PAGE_ST_CHART: String = "/simpleTest/chart/home"

    // 曲线图测试
    const val PAGE_ST_CHART_CURVE: String = "/simpleTest/chart/curve"

    // 自定义进度格测试
    const val PAGE_ST_CHART_PROGRESSCELL: String = "/simpleTest/chart/progresscell"

    // recyclerview分割线测试
    const val PAGE_ST_RECYCLERVIEW_ITEM_DECORATION: String =
        "/simpleTest/recyclerview/itemDecoration"

    // MultiEditText测试
    const val PAGE_ST_MULTIEDITTEXT: String = "/simpleTest/multiEditText"
}