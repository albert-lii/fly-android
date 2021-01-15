package com.fly.example.ui

import android.graphics.PointF
import android.os.Bundle
import com.fly.example.R
import com.fly.example.base.BaseAppBindingActivity
import com.fly.example.databinding.ActivityChartBinding

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/1/13 5:18 PM
 * @description: 图表测试页面
 * @since: 1.0.0
 */
class ChartActivity : BaseAppBindingActivity<ActivityChartBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_chart
    }

    override fun initialize(savedInstanceState: Bundle?) {
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