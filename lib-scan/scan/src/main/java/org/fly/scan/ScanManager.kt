package org.fly.scan

import android.app.Activity
import android.graphics.Bitmap
import android.os.Handler
import android.util.Log
import android.view.SurfaceHolder
import android.view.Window
import android.view.WindowManager
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import org.fly.scan.callback.CaptureCallback
import org.fly.scan.callback.OnScanResultCallback
import org.fly.scan.camera.CameraManager
import java.io.IOException

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/5 7:15 下午
 * @description: 扫描管理类
 * @since: 1.0.0
 */
class ScanManager(private val activity: Activity) : SurfaceHolder.Callback, CaptureCallback {
    private val TAG = ScanManager::class.java.simpleName

    private var cameraManager: CameraManager? = null
    private var handler: CaptureHandler? = null
    private var inactivityTimer: InactivityTimer? = null
    private var beepManager: BeepManager? = null
    private var decodeFormats: Collection<BarcodeFormat>? = null
    private var characterSet: String? = null
    var hasSurface = false
        private set

    private var scanResultCallback: OnScanResultCallback? = null

    /**
     * 保持屏幕常量
     */
    fun keepScreenOn() {
        val window: Window = activity.getWindow()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    fun init() {
        hasSurface = false
        inactivityTimer = InactivityTimer(activity)
        beepManager = BeepManager(activity)
    }

    private fun initCamera(surfaceHolder: SurfaceHolder?) {
        checkNotNull(surfaceHolder) { "No SurfaceHolder provided" }
        if (cameraManager!!.isOpen) {
            Log.w(
                TAG,
                "initCamera() while already open -- late SurfaceView callback?"
            )
            return
        }
        try {
            cameraManager!!.openDriver(surfaceHolder)
            // Creating the handler starts the preview, which can also throw a RuntimeException.
            if (handler == null) {
                handler = CaptureHandler(
                    activity,
                    decodeFormats,
                    null,
                    characterSet,
                    cameraManager,
                    this
                )
            }
        } catch (ioe: IOException) {
            Log.e(TAG, "Failed to initializing camera ", ioe)
        } catch (e: RuntimeException) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.e(TAG, "Unexpected error initializing camera", e)
        }
    }

    fun resume(surfaceHolder: SurfaceHolder) {
        // CameraManager must be initialized here, not in onCreate(). This is necessary because we don't
        // want to open the camera driver and measure the screen size if we're going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the wrong size and partially
        // off screen.
        // CameraManager must be initialized here, not in onCreate(). This is necessary because we don't
        // want to open the camera driver and measure the screen size if we're going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the wrong size and partially
        // off screen.
        cameraManager = CameraManager(activity.getApplication())

        handler = null

        beepManager!!.updatePrefs()

        inactivityTimer!!.onResume()

        decodeFormats = null
        characterSet = null

        if (hasSurface) {
            // The activity was paused but not stopped, so the surface still exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(surfaceHolder)
        } else {
            // Install the callback and wait for surfaceCreated() to init the camera.
            surfaceHolder.addCallback(this)
        }
    }

    fun pause(surfaceHolder: SurfaceHolder) {
        if (handler != null) {
            handler!!.quitSynchronously()
            handler = null
        }
        inactivityTimer!!.onPause()
        beepManager!!.close()
        cameraManager!!.closeDriver()
        // Keep for onActivityResult
        if (!hasSurface) {
            surfaceHolder.removeCallback(this)
        }
    }

    fun destroy() {
        inactivityTimer!!.shutdown()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        if (!hasSurface) {
            hasSurface = true
            initCamera(holder)
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // do nothing
        Log.i(TAG, String.format("Surface has changed, width=%s ,height=%s", width, height))
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        hasSurface = false
    }

    override fun getHandler(): Handler {
        return handler!!
    }

    override fun getCameraManager(): CameraManager {
        return cameraManager!!
    }

    /**
     * A valid barcode has been found, so give an indication of success and show the results.
     *
     * @param rawResult   The contents of the barcode.
     * @param scaleFactor amount by which thumbnail was scaled
     * @param barcode     A greyscale bitmap of the camera data which was decoded.
     */
    override fun handleDecode(rawResult: Result?, barcode: Bitmap?, scaleFactor: Float) {
        inactivityTimer!!.onActivity()
        val fromLiveScan = barcode != null
        if (fromLiveScan) {
            // Then not from history, so beep/vibrate and we have an image to draw on
            beepManager!!.playBeepSoundAndVibrate()
        }
        scanResultCallback?.getScanResult(rawResult, barcode)
    }

    /**
     * 设置扫描结果回调
     */
    fun setScanResultCallback(callback: OnScanResultCallback) {
        this.scanResultCallback = callback
    }
}