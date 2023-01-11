package org.fly.scan.callback;

import android.graphics.Bitmap;

import com.google.zxing.Result;

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/5 8:11 下午
 * @description: 扫描结果回调
 * @since: 1.0.0
 */
public interface OnScanResultCallback {
    /**
     * 获取扫描结果
     *
     * @param rawResult 扫描结果
     * @param barcode   二维码图片
     */
    void getScanResult(Result rawResult, Bitmap barcode);
}
