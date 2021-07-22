package fly.mod.app.test.chart.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import fly.mod.app.test.R
import fly.mod.app.test.databinding.StActivityProgressCellBinding
import fly.mod.lib.common.base.BaseAppBindingActivity
import fly.mod.lib.common.router.RouteConstants

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/7/9 10:14 AM
 * @description: 自定义进度格控件测试页面
 * @since: 1.0.0
 */
@Route(path = RouteConstants.PAGE_ST_CHART_PROGRESSCELL)
class ProgressCellActivity : BaseAppBindingActivity<StActivityProgressCellBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.st_activity_progress_cell
    }

    override fun initialize(savedInstanceState: Bundle?) {

    }
}