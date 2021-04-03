package fly.mod.app.main.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import fly.mod.app.main.R
import fly.mod.app.main.databinding.MActivityMainBinding
import fly.mod.lib.common.base.BaseAppBindingActivity
import fly.mod.lib.common.router.RouteConstants
import fly.mod.lib.common.router.RouterUtils
import kotlinx.coroutines.MainScope
import org.we.fly.extensions.singleClick

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/3/17 5:48 PM
 * @description: 首页
 * @since: 1.0.0
 */
@Route(path = RouteConstants.PAGE_M_MAIN)
class MainActivity : BaseAppBindingActivity<MActivityMainBinding>() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun getLayoutId(): Int {
        return R.layout.m_activity_main
    }

    override fun initialize(savedInstanceState: Bundle?) {
        firebaseAnalytics = Firebase.analytics
        addListener()
        MainScope()
    }

    private fun addListener() {
        binding.btnMicroApp.singleClick {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.METHOD, "btnMicroApp")
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle)

            RouterUtils.getInstance()
                .navigateBySingleTask(this@MainActivity, RouteConstants.PAGE_M_ARTICLE_LIST)
        }
        binding.btnTestApp.singleClick {
            RouterUtils.getInstance()
                .navigateBySingleTask(this@MainActivity, RouteConstants.PAGE_ST_TEST_HOME)
        }
    }
}