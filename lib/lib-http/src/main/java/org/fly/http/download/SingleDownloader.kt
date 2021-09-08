package org.fly.http.download

import kotlinx.coroutines.*
import org.fly.http.HttpClient
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.CancellationException

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/12 2:22 下午
 * @description: 单文件下载器
 * @since: 1.0.0
 */
open class SingleDownloader(val client: HttpClient) {
    // 下载开始
    private var startAction: (() -> Unit)? = null

    // 下载完成（成功或失败）
    private var completeAction: ((url: String, filePath: String) -> Unit)? = null

    // 下载成功
    private var successAction: ((url: String, file: File) -> Unit)? = null

    // 下载出现异常
    private var errorAction: ((url: String, cause: Throwable) -> Unit)? = null

    // 下载进度
    private var progressAction: ((currentLength: Long, totalLength: Long, progress: Int) -> Unit)? =
        null

    private var job: Job? = null

    fun onStart(action: () -> Unit): SingleDownloader {
        this.startAction = action
        return this
    }

    fun onCompletion(action: (url: String, filePath: String) -> Unit): SingleDownloader {
        this.completeAction = action
        return this
    }

    fun onSuccess(action: ((url: String, file: File) -> Unit)): SingleDownloader {
        successAction = action
        return this
    }

    fun onError(action: ((url: String, cause: Throwable) -> Unit)): SingleDownloader {
        errorAction = action
        return this
    }

    fun onProgress(action: ((currentLength: Long, totalLength: Long, progress: Int) -> Unit)): SingleDownloader {
        progressAction = action
        return this
    }

    /**
     * 开始下载
     */
    fun excute(url: String, filePath: String) {
        job = CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                download(url, filePath)
            }
        }
    }

    /**
     * 取消下载
     */
    fun cancel() {
        job?.cancel()
    }

    /**
     * 任务是否执行完毕
     */
    fun isCompleted(): Boolean {
        return job?.isCompleted ?: true
    }

    suspend fun download(url: String, filePath: String) {
        try {
            startAction?.let { action ->
                withContext(Dispatchers.Main) {
                    action.invoke()
                }
            }
            val response = client.getRequestService().downloadFile(url)
            val body = response.body()
            if (body != null) {
                val file = File(filePath)
                val inputStream = body.byteStream()
                // 文件总长度
                val totalLength = body.contentLength()
                // 当前已下载长度
                var currentLength = 0L
                // inputStream每次读取的长度
                var onceLength = 0
                val outputStream = FileOutputStream(file)
                // 缓冲区
                val buffer = ByteArray(1 * 1024)
                while (inputStream.read(buffer).also { onceLength = it } != -1) {
                    outputStream.write(buffer, 0, onceLength)
                    currentLength += onceLength
                    progressAction?.let { action ->
                        withContext(Dispatchers.Main) {
                            action.invoke(
                                currentLength,
                                totalLength,
                                (currentLength * 1f / totalLength * 100).toInt()
                            )
                        }
                    }
                }
                outputStream.close()
                completeAction?.let { action ->
                    withContext(Dispatchers.Main) {
                        action.invoke(url, filePath)
                    }
                }
                successAction?.let { action ->
                    withContext(Dispatchers.Main) {
                        action.invoke(url, file)
                    }
                }
            } else {
                completeAction?.let { action ->
                    withContext(Dispatchers.Main) {
                        action.invoke(url, filePath)
                    }
                }
            }
        } catch (cause: Throwable) {
            if (cause is CancellationException) {
                // do nothing
            } else {
                completeAction?.let { action ->
                    withContext(Dispatchers.Main) {
                        action.invoke(url, filePath)
                    }
                }
                errorAction?.let { action ->
                    withContext(Dispatchers.Main) {
                        action.invoke(url, cause)
                    }
                }
            }
        }
    }
}