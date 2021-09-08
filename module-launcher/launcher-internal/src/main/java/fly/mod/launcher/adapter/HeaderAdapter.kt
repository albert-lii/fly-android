package fly.mod.launcher.adapter

import androidx.lifecycle.LifecycleOwner
import fly.mod.launcher.R
import fly.mod.launcher.databinding.LauncherRecyclerItemHeaderBinding
import org.fly.uikit.loopview.OnPageChangeListener
import org.fly.uikit.recyclerview.BaseBindRecyclerAdapter

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/9/4 8:17 下午
 * @description: 页面的头部布局
 * @since: 1.0.0
 */
class HeaderAdapter(private val owner: LifecycleOwner) :
    BaseBindRecyclerAdapter<LauncherRecyclerItemHeaderBinding, String>() {

    private val bannerAdapter by lazy { BannerAdapter() }

    init {
        setItems(arrayListOf(""))
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.launcher_recycler_item_header
    }

    override fun onBindingItem(
        binding: LauncherRecyclerItemHeaderBinding,
        item: String,
        position: Int
    ) {
        if (binding.banner.layoutManager == null) {
            val data = arrayListOf("Let's", "Fly")
            binding.indicator.totalCount = data.size
            bannerAdapter.setItems(data)
            binding.banner.auto(
                owner = owner,
                adapter = bannerAdapter,
                listener = object : OnPageChangeListener {
                    override fun onPageSelected(position: Int) {
                        binding.indicator.transfer(position)
                    }

                    override fun onPageScrollStateChanged(state: Int) {

                    }
                })
        }
    }
}