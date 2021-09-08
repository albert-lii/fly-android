package fly.lib.common.widgets

import android.content.Context
import android.util.AttributeSet
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/9/6 6:41 下午
 * @description: -
 * @since: 1.0.0
 */
class LoadingView : LottieAnimationView {

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        repeatCount = LottieDrawable.INFINITE
        setAnimation("lottie/loading.json")
    }

    fun start() {
        playAnimation()
    }

    fun stop() {
        cancelAnimation()
    }
}