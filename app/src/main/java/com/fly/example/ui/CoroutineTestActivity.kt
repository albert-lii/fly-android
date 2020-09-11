package com.fly.example.ui

import android.os.Bundle
import android.view.View
import com.fly.example.R
import com.fly.example.base.BaseAppBVMActivity
import com.fly.example.base.BaseAppBindingActivity
import com.fly.example.databinding.ActivityCoroutineTestBinding
import com.fly.example.viewmodel.CoroutineTestViewModel
import org.we.fly.extensions.observeNonNull
import org.we.fly.extensions.singleClick

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/9/11 12:05 PM
 * @description: 协程相关测试
 * @since: 1.0.0
 */
class CoroutineTestActivity :
    BaseAppBVMActivity<ActivityCoroutineTestBinding, CoroutineTestViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_coroutine_test
    }

    override fun createViewModel(): CoroutineTestViewModel {
        return CoroutineTestViewModel()
    }

    override fun initialize(savedInstanceState: Bundle?) {
        addListener()
        addObserver()
    }

    private fun addListener() {
        binding.btn1.singleClick {
            binding.btn1.isEnabled = false
            viewModel.q1Test()
        }
        binding.btn2.singleClick {
            binding.btn2.isEnabled = false
            viewModel.q2Test()
        }
        binding.btn3.singleClick {
            binding.btn3.isEnabled = false
            viewModel.q3Test()
        }
        binding.btn4.singleClick {
            binding.btn4.isEnabled = false
            viewModel.q4Test()
        }
        binding.btn5.singleClick {
            binding.btn5.isEnabled = false
            viewModel.q5Test()
        }
        binding.btn6.singleClick {
            binding.btn6.isEnabled = false
            viewModel.q6Test()
        }
        binding.btn7.singleClick {
            binding.btn7.isEnabled = false
            viewModel.q7Test()
        }
        binding.btn8.singleClick {
            binding.btn8.isEnabled = false
            viewModel.q8Test()
        }
    }

    private fun addObserver() {
        viewModel.q1TestResult.observeNonNull(this) {
            binding.btn1.isEnabled = true
            binding.btn1.text = it
        }
        viewModel.q2TestResult.observeNonNull(this) {
            binding.btn2.isEnabled = true
            binding.btn2.text = it
        }
        viewModel.q3TestResult.observeNonNull(this) {
            binding.btn3.isEnabled = true
            binding.btn3.text = it
        }
        viewModel.q4TestResult.observeNonNull(this) {
            binding.btn4.isEnabled = true
            binding.btn4.text = it
        }
        viewModel.q5TestResult.observeNonNull(this) {
            binding.btn5.isEnabled = true
            binding.btn5.text = it
        }
        viewModel.q6TestResult.observeNonNull(this) {
            binding.btn6.isEnabled = true
            binding.btn6.text = it
        }
        viewModel.q7TestResult.observeNonNull(this) {
            binding.btn7.isEnabled = true
            binding.btn7.text = it
        }
        viewModel.q8TestResult.observeNonNull(this) {
            binding.btn8.isEnabled = true
            binding.btn8.text = it
        }
    }
}