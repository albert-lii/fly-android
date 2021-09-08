package fly.mod.launcher.ui

import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.alibaba.android.arouter.facade.annotation.Route
import fly.lib.common.base.BaseAppBindActivity
import fly.lib.common.router.RouteConstants
import fly.mod.launcher.R
import fly.mod.launcher.databinding.LauncherActivityMainBinding
import fly.mod.launcher.event.LAUNCHER_EVENT_OPEN_SIDEBAR
import org.fly.base.exts.singleClick
import org.fly.base.signal.livebus.LiveBus

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/3/17 5:48 PM
 * @description: 首页
 * @since: 1.0.0
 */
@Route(path = RouteConstants.ROUTE_MAIN)
class MainActivity : BaseAppBindActivity<LauncherActivityMainBinding>() {
    private var transaction: FragmentTransaction? = null
    private var fragmentList: ArrayList<Fragment> = arrayListOf()
    private var currentFragment: Fragment? = null

    override fun getLayoutId(): Int {
        return R.layout.launcher_activity_main
    }

    override fun initialize(savedInstanceState: Bundle?) {
        initView()
        addObserver()
        addListener()
    }

    private fun initView() {
        initFragment()
        switchFragment(0)
    }

    private fun addObserver() {
        LiveBus.get(LAUNCHER_EVENT_OPEN_SIDEBAR).observe(this) {
            toggleDrawer(true)
        }
    }

    private fun addListener() {
        binding.layoutContent.bottomNavigator.tvIndex.singleClick {

        }
        binding.layoutContent.bottomNavigator.tvSample.singleClick {

        }
    }

    private fun initFragment() {
        fragmentList.add(IndexFragment())
        transaction = supportFragmentManager.beginTransaction()
        for ((index, fragment) in fragmentList.withIndex()) {
            transaction?.add(R.id.fl_container, fragment, index.toString())
        }
    }

    /**
     * 切换Fragment
     */
    private fun switchFragment(index: Int) {
        currentFragment?.let {
            transaction?.hide(it)
        }
        currentFragment = fragmentList[index]
        transaction?.show(currentFragment!!)
            ?.commitAllowingStateLoss()
    }

    /**
     * 打开/关闭抽屉
     */
    private fun toggleDrawer(isOpen: Boolean = true) {
        if (isOpen) {
            binding.drawer.openDrawer(GravityCompat.START)
        } else {
            binding.drawer.closeDrawer(GravityCompat.START)
        }
    }
}