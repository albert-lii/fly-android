package com.fly.example.livebustest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.fly.example.Constants
import com.fly.example.R
import kotlinx.android.synthetic.main.activity_livebus_first.*
import org.we.fly.extensions.singleClick
import org.we.fly.utils.livebus.LiveBus


/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/3 2:29 PM
 * @description: --
 * @since: 1.0.0
 */
class LiveBusFirstActivity : AppCompatActivity() {
    private var recevieCountOnCREATED = 0
    private var recevieCountOnSTARTED = 0
    private lateinit var foreverObserver: Observer<String>
    private lateinit var stickyForeverObserver: Observer<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_livebus_first)
        addListener()
        initObserve()
    }

    private fun addListener() {
        btn_post.singleClick {
            recevieCountOnCREATED = 0
            recevieCountOnSTARTED = 0
            changeReceiveCountUI()
            LiveBus.get(Constants.BK_FIRST_POST_TEST).post("FirstActivity中Post测试")
            startActivity(Intent(this, LiveBusSecondActivity::class.java))
        }
    }

    private fun initObserve() {
        LiveBus.get(Constants.BK_SECOND_POST_CREATED_TEST, String::class.java)
            .observerAlwaysBeActive(true)
            .observe(this, object : Observer<String> {
                override fun onChanged(t: String?) {
                    recevieCountOnCREATED++;
                    changeReceiveCountUI()
                }
            })
        LiveBus.get(Constants.BK_SECOND_POST_STARTED_TEST, String::class.java)
            .observerAlwaysBeActive(false)
            .observe(this, object : Observer<String> {
                override fun onChanged(t: String?) {
                    recevieCountOnSTARTED++;
                    changeReceiveCountUI()
                }
            })
        LiveBus.get(Constants.BK_SECOND_POST_TEST, String::class.java)
            .observe(this, object : Observer<String> {
                override fun onChanged(t: String?) {
                    tv_observe.text = "普通订阅：${t}"
                }
            })
        LiveBus.get(Constants.BK_SECOND_POST_TEST, String::class.java)
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
        LiveBus.get(Constants.BK_SECOND_POST_TEST, String::class.java)
            .observeForever(foreverObserver)
        stickyForeverObserver = object : Observer<String> {
            override fun onChanged(t: String?) {
                tv_observe_sticky_forever.text = "StickyForever订阅：${t}"
            }
        }
        LiveBus.get(Constants.BK_SECOND_POST_TEST, String::class.java)
            .observeStickyForever(stickyForeverObserver)
    }

    private fun changeReceiveCountUI() {
        tv_receive_created.text = "CREATED状态：收到来自Second页面的Post次数：${recevieCountOnCREATED}次"
        tv_receive_started.text = "STARTED状态：收到来自Second页面的Post次数：${recevieCountOnSTARTED}次"
    }

    override fun onDestroy() {
        super.onDestroy()
        LiveBus.get(Constants.BK_SECOND_POST_TEST, String::class.java)
            .removeObserver(foreverObserver)
        LiveBus.get(Constants.BK_SECOND_POST_TEST, String::class.java)
            .removeObserver(stickyForeverObserver)
    }
}
