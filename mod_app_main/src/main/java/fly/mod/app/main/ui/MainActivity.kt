package fly.mod.app.main.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import fly.mod.app.main.R
import fly.mod.app.main.databinding.MActivityMainBinding
import fly.mod.app.main.viewmodel.ArticleListViewModel
import fly.mod.lib.common.base.BaseAppBVMActivity
import fly.mod.lib.common.router.RouteConstants
import fly.mod.lib.common.router.RouterUtils
import org.we.fly.extensions.singleClick

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/3/17 5:48 PM
 * @description: 首页
 * @since: 1.0.0
 */
@Route(path = RouteConstants.PAGE_M_MAIN)
class MainActivity : BaseAppBVMActivity<MActivityMainBinding, ArticleListViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.m_activity_main
    }

    override fun createViewModel(): ArticleListViewModel {
        return ArticleListViewModel()
    }

    override fun initialize(savedInstanceState: Bundle?) {
        addListener()
    }

    private fun addListener() {
        binding.btnMicroApp.singleClick {
            RouterUtils.getInstance()
                .navigateBySingleTask(this@MainActivity, RouteConstants.PAGE_M_ARTICLE_LIST)
        }
        binding.btnTestApp.singleClick {
            RouterUtils.getInstance()
                .navigateBySingleTask(this@MainActivity, RouteConstants.PAGE_ST_TEST_HOME)
        }
    }
}