package org.fly.eventbus.sample

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.TaskStackBuilder
import org.fly.eventbus.EventBus
import org.fly.eventbus.sample.databinding.ActivityFirstBinding

class FirstActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirstBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addListener()
        addObserver()
    }

    private fun addListener() {
        binding.btnSendOrdinary.setOnClickListener {
            EventBus.with<String>("First").post(value = "来自First的普通事件")
        }
        binding.btnSendSticky.setOnClickListener {
            EventBus.withSticky<String>("FirstSticky").post(value = "来自First的粘性事件")
        }
        binding.btnUpdateCount.setOnClickListener {
            binding.tvEventCount.text = "事件个数：${EventBus.count()}"
        }
        binding.btnNext.setOnClickListener {
//            startActivity(Intent(this, SecondActivity::class.java))

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("cloud-tr://page/home"))
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(this)
            // 合并回退栈，不然会后台会出现多个app
            stackBuilder.addNextIntentWithParentStack(intent).startActivities()
//            startActivity(intent)
        }
    }

    private fun addObserver() {
        EventBus.with<String>("First").observeInLifecycle(this) {
            binding.tvOrdinaryContent.text = it ?: "没有内容"
        }
    }
}