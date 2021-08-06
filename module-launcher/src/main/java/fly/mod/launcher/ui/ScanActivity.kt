package fly.mod.launcher.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.os.Bundle
import com.google.zxing.Result
import fly.mod.launcher.R
import org.fly.scan.BaseScanActivity
import org.fly.scan.ScanFinderView

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/4 9:32 下午
 * @description: -
 * @since: 1.0.0
 */
class ScanActivity : BaseScanActivity() {
    private lateinit var sfv_finder: ScanFinderView

    override fun getLayoutId(): Int {
        return R.layout.m_activity_scan
    }

    override fun getSurfaceViewId(): Int {
        return R.id.sfv_scan
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sfv_finder = findViewById(R.id.sfv_finder)
    }

    override fun onDestroy() {
        sfv_finder.stopScanAnim()
        super.onDestroy()
    }

    override fun getScanResult(rawResult: Result?, barcode: Bitmap?) {
        AlertDialog.Builder(this)
            .setTitle("扫码结果")
            .setMessage(rawResult?.text ?: "")
            .setPositiveButton("知道了", { dialog, which ->
                dialog.dismiss()
            })
            .show()
    }
}