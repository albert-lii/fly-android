package org.fly.scan;

import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

import org.fly.scan.callback.OnScanResultCallback;

/**
 * 扫描页面
 *
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/4 12:08 下午
 * @since: 1.0.0
 */
public abstract class BaseScanActivity extends AppCompatActivity implements OnScanResultCallback {
    private ScanManager scanManager;
    private SurfaceView surfaceView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scanManager = new ScanManager(this);
        // 保持屏幕常量
        scanManager.keepScreenOn();
        setContentView(getLayoutId());
        scanManager.init();
        surfaceView = (SurfaceView) findViewById(getSurfaceViewId());
        scanManager.setScanResultCallback(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        scanManager.resume(surfaceView.getHolder());
    }

    @Override
    protected void onPause() {
        scanManager.pause(surfaceView.getHolder());
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        scanManager.destroy();
        super.onDestroy();
    }

    public abstract int getLayoutId();

    public abstract int getSurfaceViewId();
}

