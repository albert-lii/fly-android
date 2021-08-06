package fly.mod.launcher.ui

import android.content.Intent
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import fly.mod.launcher.R
import fly.mod.launcher.databinding.MActivityMainBinding
import fly.mod.lib.common.base.BaseAppBindingActivity
import fly.mod.lib.common.router.RouteConstants
import fly.mod.lib.common.router.RouterUtils
import org.fly.base.extensions.singleClick

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/3/17 5:48 PM
 * @description: 首页
 * @since: 1.0.0
 */
@Route(path = RouteConstants.PAGE_M_MAIN)
class MainActivity : BaseAppBindingActivity<MActivityMainBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.m_activity_main
    }

    override fun initialize(savedInstanceState: Bundle?) {
        addListener()
    }

    private fun addListener() {
        binding.btnMicroApp.singleClick {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.METHOD, "btnMicroApp")

//            RouterUtils.getInstance()
//                .navigateBySingleTask(this@MainActivity, RouteConstants.PAGE_M_ARTICLE_LIST)
            startActivity(Intent(this, ScanActivity::class.java))
        }
        binding.btnTestApp.singleClick {
            RouterUtils.getInstance()
                .navigateBySingleTask(this@MainActivity, RouteConstants.PAGE_ST_TEST_HOME)
        }
    }
}