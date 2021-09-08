package fly.lib.common.router

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter
import java.io.Serializable
import java.util.*

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/3/15 5:57 PM
 * @description: Postcard包装类
 * @since: 1.0.0
 */
class PostcardWrapper {
    private var postcard: Postcard

    constructor(path: String) {
        postcard = ARouter.getInstance().build(path)
    }


    fun withFlags(flag: Int): PostcardWrapper {
        postcard.withFlags(flag)
        return this
    }

    fun withObject(key: String, value: Any?): PostcardWrapper {
        postcard.withObject(key, value)
        return this
    }

    fun withInt(key: String, value: Int): PostcardWrapper {
        postcard.withInt(key, value)
        return this
    }

    fun withDouble(key: String, value: Double): PostcardWrapper {
        postcard.withDouble(key, value)
        return this
    }

    fun withFloat(key: String, value: Float): PostcardWrapper {
        postcard.withFloat(key, value)
        return this
    }

    fun withString(key: String, value: String?): PostcardWrapper {
        postcard.withString(key, value)
        return this
    }

    fun withBoolean(key: String, value: Boolean): PostcardWrapper {
        postcard.withBoolean(key, value)
        return this
    }

    fun withParcelable(key: String, value: Parcelable): PostcardWrapper {
        postcard.withParcelable(key, value)
        return this
    }

    fun withSerializable(key: String, value: Serializable): PostcardWrapper {
        postcard.withSerializable(key, value)
        return this
    }

    fun withStringArrayList(key: String, value: ArrayList<String>): PostcardWrapper {
        postcard.withStringArrayList(key, value)
        return this
    }

    fun withAction(action: String): PostcardWrapper {
        postcard.withAction(action)
        return this
    }

    fun getAction(): String {
        return postcard.action
    }

    fun greenChannel(): PostcardWrapper {
        postcard.greenChannel()
        return this
    }

    fun isGreenChannel(): Boolean {
        return postcard.isGreenChannel()
    }

    fun singleTop(): PostcardWrapper {
        postcard.withFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        return this
    }

    fun singleTask(): PostcardWrapper {
        postcard.withFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        return this
    }

    fun navigation(context: Context):Any? {
        return postcard.navigation(context)
    }

    fun getPostcard(): Postcard {
        return postcard
    }
}