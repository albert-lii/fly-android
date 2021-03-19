package fly.mod.app.simpletest.chart.ui

import android.graphics.PointF
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import fly.mod.app.simpletest.R
import fly.mod.app.simpletest.databinding.StActivityCurveBinding
import fly.mod.lib.common.base.BaseAppBindingActivity
import fly.mod.lib.common.router.RouteConstants

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/1/13 5:18 PM
 * @description: 曲线图测试页面
 * @since: 1.0.0
 */
@Route(path = RouteConstants.PAGE_ST_CHART_CURVE)
class CurveActivity : BaseAppBindingActivity<StActivityCurveBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.st_activity_curve
    }

    override fun initialize(savedInstanceState: Bundle?) {
        setData()
    }

    private fun setData() {
        binding.curveChart.xAxisData = arrayListOf("01-01", "01-02", "01-03")
        binding.curveChart.yAxisData = arrayListOf("-20%", "0%", "20%")
        binding.curveChart.dataPoints = arrayListOf(
            PointF(0f, 90f),
            PointF(30f, 30f),
            PointF(60f, 100f),
            PointF(90f, 160f),
            PointF(120f, 80f),
            PointF(150f, 50f),
            PointF(180f, 180f),
            PointF(210f, 300f),
            PointF(240f, 230f),
            PointF(270f, 400f)
        )
        binding.curveChart.build()
    }
}