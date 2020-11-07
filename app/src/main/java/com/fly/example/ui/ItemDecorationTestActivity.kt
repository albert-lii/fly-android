package com.fly.example.ui

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fly.example.R
import com.fly.example.base.BaseAppBindingActivity
import com.fly.example.databinding.ActivityItemdecorationTestBinding
import com.fly.example.databinding.RecyclerItemItemdecorationTestBinding
import org.we.fly.base.ui.recyclerview.BaseBindingAdapter
import org.we.fly.widget.recyclerview.SpaceItemDecoration

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/11/4 11:28 PM
 * @description: RecyclerView分割线测试
 * @since: 1.0.0
 */
class ItemDecorationTestActivity : BaseAppBindingActivity<ActivityItemdecorationTestBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_itemdecoration_test
    }

    override fun initialize(savedInstanceState: Bundle?) {
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
            addItemDecoration(SpaceItemDecoration(space = 40))
            layoutManager = LinearLayoutManager(this@ItemDecorationTestActivity)
            this.adapter = MyAdapter().apply { setItems(list) }
        }
        binding.rvGrid.run {
            addItemDecoration(SpaceItemDecoration(space = 40))
            layoutManager = GridLayoutManager(this@ItemDecorationTestActivity, 3)
            this.adapter = MyAdapter().apply { setItems(list) }
        }
        binding.rvStagger.run {
            addItemDecoration(SpaceItemDecoration(space = 40))
            layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
            this.adapter = MyAdapter().apply { setItems(list) }
        }
    }

    private inner class MyAdapter :
        BaseBindingAdapter<RecyclerItemItemdecorationTestBinding, String?>() {

        override fun getLayoutId(viewType: Int): Int {
            return R.layout.recycler_item_itemdecoration_test
        }

        override fun onBindItem(
            binding: RecyclerItemItemdecorationTestBinding?,
            item: String?,
            position: Int
        ) {
            binding!!.tvContent.text = item
        }
    }
}