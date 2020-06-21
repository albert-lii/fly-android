package org.we.fly.base.ui

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/20 12:08 PM
 * @description: Activity的封装
 * @since: 1.0.0
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        init(savedInstanceState)
    }

    protected abstract @LayoutRes
    fun getLayoutId(): Int

    /**
     *  初始化操作
     */
    protected abstract fun init(savedInstanceState: Bundle?)
}