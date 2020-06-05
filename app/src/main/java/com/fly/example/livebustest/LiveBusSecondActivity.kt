package com.fly.example.livebustest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.fly.example.Constants
import com.fly.example.R
import kotlinx.android.synthetic.main.activity_livebus_second.*
import org.we.fly.utils.livebus.LiveBus


/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/3 2:29 PM
 * @description: --
 * @since: 1.0.0
 */
class LiveBusSecondActivity : AppCompatActivity() {
    private lateinit var foreverObserver: Observer<String>
    private lateinit var stickyForeverObserver: Observer<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_livebus_second)
        addListener()
        initObserve()
    }

    private fun addListener() {
        btn_post_lifecycle.setOnClickListener {
            LiveBus.get(Constants.BK_SECOND_POST_CREATED_TEST)
                .post(null)
            LiveBus.get(Constants.BK_SECOND_POST_STARTED_TEST)
                .post(null)
        }
        btn_post_end.setOnClickListener {
            LiveBus.get(Constants.BK_SECOND_POST_TEST).post("SecondActivity的Post测试 END")
            finish()
        }
    }

    private fun initObserve() {
        LiveBus.get(Constants.BK_FIRST_POST_TEST, String::class.java)
            .observeSticky(this, object : Observer<String> {
                override fun onChanged(t: String?) {
                    tv_observe.text = "普通订阅：${t}"
                }

            })
        LiveBus.get(Constants.BK_FIRST_POST_TEST, String::class.java)
            .observeSticky(this, object : Observer<String> {
                override fun onChanged(t: String?) {
                    tv_observe_sticky.text = "Sticky订阅：${t}"
                }
            })
        foreverObserver = object : Observer<String> {
            override fun onChanged(t: String?) {
                tv_observe_forever.text = "Forever订阅：${t}"
            }
        }
        LiveBus.get(Constants.BK_FIRST_POST_TEST, String::class.java)
            .observeForever(foreverObserver)
        stickyForeverObserver = object : Observer<String> {
            override fun onChanged(t: String?) {
                tv_observe_sticky_forever.text = "StickyForever订阅：${t}"
            }
        }
        LiveBus.get(Constants.BK_FIRST_POST_TEST, String::class.java)
            .observeStickyForever(stickyForeverObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        LiveBus.get(Constants.BK_SECOND_POST_TEST, String::class.java)
            .removeObserver(foreverObserver)
        LiveBus.get(Constants.BK_SECOND_POST_TEST, String::class.java)
            .removeObserver(stickyForeverObserver)
    }
}
