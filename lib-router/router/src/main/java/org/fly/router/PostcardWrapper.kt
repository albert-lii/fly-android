package org.fly.router

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.alibaba.android.arouter.launcher.ARouter
import java.io.Serializable
import java.util.ArrayList

/**
 * Postcard包装类
 *
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/20 9:47 下午
 * @since: 1.0.0
 */
class PostcardWrapper {
    private var postcard: Postcard
    private var callback: SimpleRouteCallback? = null
    private var defNavigationCallback: NavigationCallback = object : NavigationCallback {
        override fun onLost(postcard: Postcard?) {
            callback?.onLost(postcard)
        }

        override fun onFound(postcard: Postcard?) {
            callback?.onFound(postcard)
        }

        override fun onInterrupt(postcard: Postcard?) {
            callback?.onInterrupt(postcard)
        }

        override fun onArrival(postcard: Postcard?) {
            callback?.onArrival(postcard)
        }
    }

    constructor(path: String) {
        postcard = ARouter.getInstance().build(path)
    }

    constructor(uri: Uri) {
        postcard = ARouter.getInstance().build(uri)
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

    fun setCallback(callback: SimpleRouteCallback): PostcardWrapper {
        this.callback = callback
        return this
    }

    fun navigation(): Any {
        return navigation(null)
    }

    fun navigation(context: Context?): Any {
        return navigation(context, defNavigationCallback)
    }

    fun navigation(context: Context?, callback: NavigationCallback?): Any {
        return postcard.navigation(context, callback)
    }

    fun navigation(activity: Activity, requestCode: Int) {
        postcard.navigation(activity, requestCode, defNavigationCallback)
    }

    fun navigation(activity: Activity, requestCode: Int = -1, callback: NavigationCallback?) {
        postcard.navigation(activity, requestCode, callback)
    }

    fun getPostcard(): Postcard {
        return postcard
    }
}