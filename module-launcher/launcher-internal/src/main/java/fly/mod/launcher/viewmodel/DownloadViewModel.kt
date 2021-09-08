package fly.mod.launcher.viewmodel

import android.os.Environment
import androidx.lifecycle.MutableLiveData
import fly.lib.common.http.FlyHttp
import fly.lib.common.base.BaseAppViewModel
import org.fly.base.utils.AppUtils
import org.fly.http.download.SingleDownloader

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/12 5:47 下午
 * @description: -
 * @since: 1.0.0
 */
class DownloadViewModel : BaseAppViewModel() {
    var progressLD = MutableLiveData<String>()
    private val downloader by lazy { SingleDownloader(FlyHttp.getInstance().getClient()) }

    fun download() {
        launchOnUI {
            downloader
                .onStart {
                    progressLD.value = AppUtils.getContext()
                        .getExternalFilesDirs(Environment.MEDIA_MOUNTED)[0]!!.absolutePath
                }
                .onSuccess { url, file ->
                    showToast("下载成功")
                }
                .onError { url, cause ->
                    progressLD.value = cause.message ?: "下载失败"
                }
                .onProgress { currentLength, totalLength, progress ->
                    progressLD.value = progress.toString() + "%"
                }
                .excute(
                    "https://ftp.binance.com/electron-desktop/mac/production/binance.dmg",
                    AppUtils.getContext()
                        .getExternalFilesDirs(Environment.MEDIA_MOUNTED)[0]!!.absolutePath + "/test.dmg"
                )
        }

    }

    fun cancel() {
        downloader.cancel()
    }
}