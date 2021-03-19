package fly.mod.app.simpletest.recyclerview.ui

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import fly.mod.app.simpletest.R
import fly.mod.app.simpletest.databinding.StActivityRecyclerviewDecorationBinding
import fly.mod.app.simpletest.databinding.StRecyclerItemItemdecorationBinding
import fly.mod.lib.common.base.BaseAppBindingActivity
import fly.mod.lib.common.router.RouteConstants
import org.we.fly.base.ui.recyclerview.BaseBindingAdapter
import org.we.fly.widget.recyclerview.SpaceItemDecoration

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/11/4 11:28 PM
 * @description: RecyclerView分割线测试
 * @since: 1.0.0
 */
@Route(path = RouteConstants.PAGE_ST_RECYCLERVIEW_ITEM_DECORATION)
class RecyclerViewDecorationActivity : BaseAppBindingActivity<StActivityRecyclerviewDecorationBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.st_activity_recyclerview_decoration
    }

    override fun initialize(savedInstanceState: Bundle?) {
        setData()
    }

    private fun setData() {
        val adapter = MyAdapter()
        val list = ArrayList<String?>()
        list.add("测试")
        list.add("测试测试测试测试")
        list.add("测试测试测试测试测试测试测试")
        list.add("测试测试测试")
        list.add("测试测试测试")
        list.add("测试测试测试测试测试测试")
        list.add("测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试")
        list.add("测试")
        list.add("测试测试")
        adapter.setItems(list)
        binding.rvLinear.run {
            addItemDecoration(SpaceItemDecoration(space = 10))
            layoutManager = LinearLayoutManager(this@RecyclerViewDecorationActivity)
            this.adapter = MyAdapter().apply { setItems(list) }
        }
        binding.rvGrid.run {
            addItemDecoration(SpaceItemDecoration(space = 10))
            layoutManager = GridLayoutManager(this@RecyclerViewDecorationActivity, 3)
            this.adapter = MyAdapter().apply { setItems(list) }
        }
        binding.rvStagger.run {
            addItemDecoration(SpaceItemDecoration(space = 10))
            layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
            this.adapter = MyAdapter().apply { setItems(list) }
        }
    }

    private inner class MyAdapter :
        BaseBindingAdapter<StRecyclerItemItemdecorationBinding, String?>() {

        override fun getLayoutId(viewType: Int): Int {
            return R.layout.st_recycler_item_itemdecoration
        }

        override fun onBindItem(
            binding: StRecyclerItemItemdecorationBinding?,
            item: String?,
            position: Int
        ) {
            binding!!.tvContent.text = item
        }
    }
}