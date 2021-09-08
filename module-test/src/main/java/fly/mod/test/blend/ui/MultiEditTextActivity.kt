package fly.mod.test.blend.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import fly.mod.test.R
import fly.mod.test.databinding.StActivityMultiEdittextBinding
import fly.lib.common.base.BaseAppBindActivity
import fly.lib.common.router.RouteConstants
import org.fly.base.utils.countdown.CountdownUtils

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/4/21 2:32 PM
 * @description: 多功能输入框
 * @since: 1.0.0
 */
@Route(path = RouteConstants.PAGE_ST_MULTIEDITTEXT)
class MultiEditTextActivity : BaseAppBindActivity<StActivityMultiEdittextBinding>() {
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


        val onTick1: (e: CountdownUtils.CountDownEvent) -> Unit = {
//            val sec = it.millisUntilFinished / 1000
//            if (sec < 1) {
//                binding.metVerify1.getRightButtonView().text = "发送验证码"
//                binding.metVerify1.getRightButtonView().isEnabled = true
//            } else {
//                binding.metVerify1.getRightButtonView().text = "${sec}s"
//            }
        }


        val onTick2: (e: CountdownUtils.CountDownEvent) -> Unit = {
//            val sec = it.millisUntilFinished / 1000
//            if (sec < 1) {
//                binding.metVerify2.getRightButtonView().text = "发送验证码"
//                binding.metVerify2.getRightButtonView().isEnabled = true
//            } else {
//                binding.metVerify2.getRightButtonView().text = "${sec}s"
//            }
        }

    }
}