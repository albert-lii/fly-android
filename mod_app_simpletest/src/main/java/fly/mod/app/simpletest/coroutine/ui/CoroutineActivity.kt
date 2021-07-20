package fly.mod.app.simpletest.coroutine.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import fly.mod.app.simpletest.R
import fly.mod.app.simpletest.databinding.StActivityCoroutineBinding
import fly.mod.lib.common.base.BaseAppBindingActivity
import fly.mod.lib.common.router.RouteConstants
import fly.mod.lib.common.router.RouterUtils
import org.fly.base.extensions.singleClick

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/9/11 12:05 PM
 * @description: 简单的协程测试
 * @since: 1.0.0
 */
@Route(path = RouteConstants.PAGE_ST_COROUTINE)
class CoroutineActivity : BaseAppBindingActivity<StActivityCoroutineBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.st_activity_coroutine
    }

    override fun initialize(savedInstanceState: Bundle?) {
        addListener()
    }

    private fun addListener() {
        binding.btn1.singleClick {
            RouterUtils.getInstance()
                .navigate(this@CoroutineActivity, RouteConstants.PAGE_ST_COROUTINE_TEST1)
        }
        binding.btn2.singleClick {
            RouterUtils.getInstance()
                .navigate(this@CoroutineActivity, RouteConstants.PAGE_ST_COROUTINE_TEST2)
        }
    }
}