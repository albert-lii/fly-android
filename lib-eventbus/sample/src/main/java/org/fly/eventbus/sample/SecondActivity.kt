package org.fly.eventbus.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import org.fly.eventbus.ActiveState
import org.fly.eventbus.EventBus
import org.fly.eventbus.sample.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding
    private var receiveOrder = 0
    private val EVENT_KEY = "FirstSticky"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvEventCount.text = "事件个数：${EventBus.count()}"
        addListener()
        addObserver()
    }

    private fun addListener() {
        binding.btnNext.setOnClickListener {
            finish()
        }
    }

    private fun addObserver() {
        EventBus.withSticky<String>(EVENT_KEY)
            .observeInLifecycle(this, minActiveState = ActiveState.CREATED) {
                receiveOrder++
                binding.tvOnCreate.isVisible = true
                binding.tvOnCreate.text =
                    "onCreate中接收顺序: ${receiveOrder}"
            }
        EventBus.withSticky<String>(EVENT_KEY)
            .observeInLifecycle(this, minActiveState = ActiveState.STARTED) {
                receiveOrder++
                binding.tvOnStart.isVisible = true
                binding.tvOnStart.text =
                    "onStart中接收顺序: ${receiveOrder}"
            }
        EventBus.withSticky<String>(EVENT_KEY)
            .observeInLifecycle(this, minActiveState = ActiveState.RESUMED) {
                receiveOrder++
                binding.tvOnResume.isVisible = true
                binding.tvOnResume.text =
                    "onResume中接收顺序: ${receiveOrder}"
            }
        EventBus.withSticky<String>(EVENT_KEY)
            .observeInLifecycle(this, minActiveState = ActiveState.DESTROYED) {
                receiveOrder++
                binding.tvOnDestory.isVisible = true
                binding.tvOnDestory.text =
                    "onDestroy中接收顺序: ${receiveOrder}"
            }
    }
}