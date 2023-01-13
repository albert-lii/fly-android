package org.fly.utils.exts

import android.content.Context
import android.widget.Toast
import dagger.hilt.android.EntryPointAccessors
import org.fly.utils.LogUtils


inline fun <reified T> fromApplicationSafe(context: Context): T? {
    return try {
        EntryPointAccessors.fromApplication(context, T::class.java)
    } catch (e: IllegalStateException) {
        if (LogUtils.isDebug) {
            Toast.makeText(
                context,
                "IllegalStateException:The implementation of ${T::class.simpleName} cannot be found",
                Toast.LENGTH_LONG
            ).show()
        }
        LogUtils.e("IllegalStateException:The implementation of ${T::class.simpleName} cannot be found \n ${e.stackTrace}")
        null
    } catch (e: Exception) {
        if (LogUtils.isDebug) {
            Toast.makeText(
                context,
                "Exception:The implementation of ${T::class.simpleName} cannot be found",
                Toast.LENGTH_LONG
            ).show()
        }
        LogUtils.e("Exception:The implementation of ${T::class.simpleName} cannot be found \n ${e.stackTrace}")
        null
    }
}