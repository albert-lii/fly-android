package org.fly.base.exts

import android.content.Context
import dagger.hilt.android.EntryPointAccessors
import org.fly.base.utils.AppUtils
import org.fly.base.utils.LogUtils
import org.fly.base.utils.ToastUtils

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/16 2:49 下午
 * @description: Hilt的相关扩展
 * @since: 1.0.0
 */

inline fun <reified T> fromApplicationSafe(context: Context): T? {
    return try {
        EntryPointAccessors.fromApplication(context, T::class.java)
    } catch (e: IllegalStateException) {
        if (AppUtils.isDebug()) {
            ToastUtils.showToast(
                context,
                "IllegalStateException:The implementation of ${T::class.simpleName} cannot be found",
                true
            )
        }
        LogUtils.e("IllegalStateException:The implementation of ${T::class.simpleName} cannot be found \n ${e.stackTrace}")
        null
    } catch (e: Exception) {
        if (AppUtils.isDebug()) {
            ToastUtils.showToast(
                context,
                "Exception:The implementation of ${T::class.simpleName} cannot be found",
                true
            )
        }
        LogUtils.e("Exception:The implementation of ${T::class.simpleName} cannot be found \n ${e.stackTrace}")
        null
    }
}