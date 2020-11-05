package com.fly.example.ui

import android.os.Bundle
import com.fly.example.R
import com.fly.example.base.BaseAppBindingActivity
import com.fly.example.databinding.ActivityItemdecorationTestBinding
import com.fly.example.databinding.RecyclerItemItemdecorationTestBinding
import org.we.fly.base.ui.recyclerview.BaseBindingAdapter

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/11/4 11:28 PM
 * @description: -
 * @since: 1.0.0
 */
class ItemDecorationTestActivity : BaseAppBindingActivity<ActivityItemdecorationTestBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_itemdecoration_test
    }

    override fun initialize(savedInstanceState: Bundle?) {
        val adapter = MyAdapter()
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


        }
    }
}