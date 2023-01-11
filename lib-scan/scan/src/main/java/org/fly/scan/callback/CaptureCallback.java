package org.fly.scan.callback;

import android.graphics.Bitmap;
import android.os.Handler;

import com.google.zxing.Result;

import org.fly.scan.camera.CameraManager;

/**
 * 扫描信息捕获回调
 *
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/2 9:13 下午
 * @description: 页面需要实现这个callback，为应用提供必要的信息
 * @since: 1.0.0
 */
public interface CaptureCallback {

    Handler getHandler();

    CameraManager getCameraManager();

    /**
     * 解码
     *
     * @param rawResult
     * @param barcode
     * @param scaleFactor
     */
    void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor);
}
