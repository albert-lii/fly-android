package fly.mod.test.blend.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import fly.mod.test.R
import fly.mod.test.databinding.StActivityTestHomeBinding
import fly.lib.common.base.BaseAppBindActivity
import fly.lib.common.router.RouteConstants
import fly.lib.common.router.RouterUtils
import org.fly.base.exts.singleClick

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/3 2:29 PM
 * @description: 测试主页面
 * @since: 1.0.0
 */
@Route(path = RouteConstants.PAGE_ST_TEST_HOME)
class TestHomeActivity : BaseAppBindActivity<StActivityTestHomeBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.st_activity_test_home
    }

    override fun initialize(savedInstanceState: Bundle?) {
        addListener()
    }

    private fun addListener() {
        binding.btnLivebus.singleClick {
            RouterUtils.getInstance()
                .navigation(this@TestHomeActivity, RouteConstants.PAGE_ST_LIVEBUS_TEST1)
        }
        binding.btnCoroutineTest.singleClick {
            RouterUtils.getInstance()
                .navigation(this@TestHomeActivity, RouteConstants.PAGE_ST_COROUTINE)
        }
        binding.btnChartTest.singleClick {
            RouterUtils.getInstance()
                .navigation(this@TestHomeActivity, RouteConstants.PAGE_ST_CHART)
        }
        binding.btnItemdecorationTest.singleClick {
            RouterUtils.getInstance()
                .navigation(
                    this@TestHomeActivity,
                    RouteConstants.PAGE_ST_RECYCLERVIEW_ITEM_DECORATION
                )
        }
        binding.btnMultiedittextTest.singleClick {
            RouterUtils.getInstance()
                .navigation(
                    this@TestHomeActivity,
                    RouteConstants.PAGE_ST_MULTIEDITTEXT
                )
        }
    }
}
