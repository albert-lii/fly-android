package org.fly.scan.sample

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import org.fly.scan.BaseScanActivity
import org.fly.scan.ScanFinderView

class ScanActivity : BaseScanActivity() {
    private lateinit var sfv_finder: ScanFinderView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sfv_finder = findViewById(R.id.sfv_finder)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_scan
    }

    override fun getSurfaceViewId(): Int {
        return R.id.sfv_scan
    }

    override fun getScanResult(rawResult: com.google.zxing.Result?, barcode: Bitmap?) {
        AlertDialog.Builder(this)
            .setTitle("扫码结果")
            .setMessage(rawResult?.text ?: "")
            .setPositiveButton("知道了") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroy() {
        sfv_finder.stopScanAnim()
        super.onDestroy()
    }
}