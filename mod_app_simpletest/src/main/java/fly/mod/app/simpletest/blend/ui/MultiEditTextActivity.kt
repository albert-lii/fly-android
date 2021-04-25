package fly.mod.app.simpletest.blend.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import fly.mod.app.simpletest.R
import fly.mod.app.simpletest.databinding.StActivityMultiEdittextBinding
import fly.mod.lib.common.base.BaseAppBindingActivity
import fly.mod.lib.common.router.RouteConstants

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/4/21 2:32 PM
 * @description: 多功能输入框
 * @since: 1.0.0
 */
@Route(path = RouteConstants.PAGE_ST_MULTIEDITTEXT)
class MultiEditTextActivity : BaseAppBindingActivity<StActivityMultiEdittextBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.st_activity_multi_edittext
    }

    override fun initialize(savedInstanceState: Bundle?) {
        addListener()
    }

    private fun addListener() {
        binding.metNumber.doAfterInputChanged { et, s ->
            if (et.text.isEmpty()) {
                binding.metNumber.showError(false)
            } else {
                binding.metNumber.showError(true)
                binding.metNumber.setError("*输入错误")
            }
        }
        binding.metSearch.doRightButtonClick { et, btn ->
            if (et.text.toString().trim().isEmpty()) {
                showToast("搜索内容不能为空")
            } else {
                showToast("搜索已点击")
            }
        }

        val onTick1: (millisUntilFinished: Long) -> Unit = {
            val sec = it / 1000
            if (sec < 1) {
                binding.metVerify1.getRightButtonView().text = "发送验证码"
                binding.metVerify1.getRightButtonView().isEnabled = true
            } else {
                binding.metVerify1.getRightButtonView().text = "${sec}s"
            }
        }
        binding.metVerify1.doRightButtonClick { et, btn ->
            binding.metVerify1.startCountDown("test1", 10 * 1000, 1000, this, onTick1)
            btn.isEnabled = false
        }
        binding.metVerify1.restoreCountDown("test1", this, {
            binding.metVerify1.getRightButtonView().isEnabled = false
            onTick1(it)
        })

        val onTick2: (millisUntilFinished: Long) -> Unit = {
            val sec = it / 1000
            if (sec < 1) {
                binding.metVerify2.getRightButtonView().text = "发送验证码"
                binding.metVerify2.getRightButtonView().isEnabled = true
            } else {
                binding.metVerify2.getRightButtonView().text = "${sec}s"
            }
        }
        binding.metVerify2.doRightButtonClick { et, btn ->
            binding.metVerify2.startCountDown("test2", 15 * 1000, 1000, this, onTick2)
            btn.isEnabled = false
        }
        binding.metVerify2.restoreCountDown("test2", this, {
            binding.metVerify2.getRightButtonView().isEnabled = false
            onTick2(it)
        })
    }
}