package fly.mod.launcher.api

import android.content.Context
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.fly.base.exts.fromApplicationSafe

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/14 2:08 下午
 * @description: -
 * @since: 1.0.0
 */
interface LauncherApiService {

    fun scanQrCode()
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface LoginApiServiceEntryPoint {

    fun launcherApiService(): LauncherApiService
}

fun loginApiService(context: Context) =
    fromApplicationSafe<LoginApiServiceEntryPoint>(context)?.launcherApiService()



