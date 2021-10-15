package fly.mod.launcher.adapter

import fly.mod.launcher.R
import fly.mod.launcher.databinding.LauncherRecyclerItemBannerBinding
import org.fly.base.exts.toColor
import org.fly.uikit.recyclerview.BaseBindRecyclerAdapter

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/9/3 9:39 下午
 * @description: Banner适配器
 * @since: 1.0.0
 */
class BannerAdapter : BaseBindRecyclerAdapter<LauncherRecyclerItemBannerBinding, String>() {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.launcher_recycler_item_banner
    }

    override fun onBindingItem(
        binding: LauncherRecyclerItemBannerBinding,
        item: String,
        position: Int
    ) {
        binding.flFrame.setNormalBgColor(
            if (position % 2 == 0) {
                R.color.primary_color.toColor(binding.flFrame.context)
            } else {
                R.color.base_yellow.toColor(binding.flFrame.context)
            }
        )
        binding.flFrame.buildRoundBackground()
        binding.tvInfo.text = item
    }
}