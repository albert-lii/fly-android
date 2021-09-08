package fly.mod.test.coroutine.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import fly.mod.test.R
import fly.mod.test.databinding.StActivityCoroutineBinding
import fly.lib.common.base.BaseAppBindActivity
import fly.lib.common.router.RouteConstants
import fly.lib.common.router.RouterUtils
import org.fly.base.exts.singleClick

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/9/11 12:05 PM
 * @description: 简单的协程测试
 * @since: 1.0.0
 */
@Route(path = RouteConstants.PAGE_ST_COROUTINE)
class CoroutineActivity : BaseAppBindActivity<StActivityCoroutineBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.st_activity_coroutine
    }

    override fun initialize(savedInstanceState: Bundle?) {
        addListener()
    }

    private fun addListener() {
        binding.btn1.singleClick {
            RouterUtils.getInstance()
                .navigation(this@CoroutineActivity, RouteConstants.PAGE_ST_COROUTINE_TEST1)
        }
        binding.btn2.singleClick {
            RouterUtils.getInstance()
                .navigation(this@CoroutineActivity, RouteConstants.PAGE_ST_COROUTINE_TEST2)
        }
    }
}