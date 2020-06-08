package com.fly.example

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fly.example.livebustest.LiveBusFirstActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.we.fly.extensions.singleClick

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/3 2:29 PM
 * @description: --
 * @since: 1.0.0
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addListener()
    }

    private fun addListener() {
        btn_livebus.singleClick {
            jumpToActivity(LiveBusFirstActivity::class.java)
        }
    }

    private fun <T> jumpToActivity(cls: Class<T>) {
        startActivity(Intent(this, cls))
    }
}
