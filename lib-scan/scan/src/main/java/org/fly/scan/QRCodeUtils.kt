package org.fly.scan

import android.graphics.Bitmap
import android.graphics.Color
import android.text.TextUtils
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import java.util.*

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/7/28 5:39 下午
 * @description: 二维码相关工具类
 * @since: 1.0.0
 */
object QRCodeUtils {

    /**
     * 生成自定义二维码
     *
     * @param content                字符串内容
     * @param width                  二维码宽度
     * @param height                 二维码高度
     * @param characterSet          编码方式（一般使用UTF-8）
     * @param errorCorrectionLevel 容错率 L：7% M：15% Q：25% H：35%
     * @param margin                 空白边距（二维码与边框的空白区域）
     * @param colorBlack            黑色色块的颜色
     * @param colorWhite            白色色块的颜色
     * @param logoBitmap             logo图片（传null时不添加logo）
     * @param logoPercent            logo所占百分比
     * @param bitmapBlack           用来代替黑色色块的图片（传null时不代替）
     * @return
     */
    @JvmStatic
    fun createQRCodeBitmap(
        content: String,
        width: Int,
        height: Int,
        characterSet: String = "UTF-8",
        errorCorrectionLevel: String = "L",
        margin: String = "0",
        colorBlack: Int = Color.BLACK,
        colorWhite: Int = Color.WHITE,
        logoBitmap: Bitmap? = null,
        logoPercent: Float = 0.2f,
        bitmapBlack: Bitmap? = null
    ): Bitmap? {
        var bitmap_black = bitmapBlack
        // 字符串内容判空
        if (TextUtils.isEmpty(content)) {
            return null
        }
        // 宽和高>=0
        if (width < 0 || height < 0) {
            return null
        }
        try {
            /** 1.设置二维码相关配置,生成BitMatrix(位矩阵)对象  */
            val hints = Hashtable<EncodeHintType, String>()
            // 字符转码格式设置
            if (!TextUtils.isEmpty(characterSet)) {
                hints[EncodeHintType.CHARACTER_SET] = characterSet
            }
            // 容错率设置
            if (!TextUtils.isEmpty(errorCorrectionLevel)) {
                hints[EncodeHintType.ERROR_CORRECTION] = errorCorrectionLevel
            }
            // 空白边距设置
            if (!TextUtils.isEmpty(margin)) {
                hints[EncodeHintType.MARGIN] = margin
            }
            /** 2.将配置参数传入到QRCodeWriter的encode方法生成BitMatrix(位矩阵)对象  */
            val bitMatrix =
                QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints)

            /** 3.创建像素数组,并根据BitMatrix(位矩阵)对象为数组元素赋颜色值  */
            if (bitmap_black != null) {
                //从当前位图按一定的比例创建一个新的位图
                bitmap_black = Bitmap.createScaledBitmap(bitmap_black, width, height, false)
            }
            val pixels = IntArray(width * height)
            for (y in 0 until height) {
                for (x in 0 until width) {
                    //bitMatrix.get(x,y)方法返回true是黑色色块，false是白色色块
                    if (bitMatrix.get(x, y)) {// 黑色色块像素设置
                        if (bitmap_black != null) {//图片不为null，则将黑色色块换为新位图的像素。
                            pixels[y * width + x] = bitmap_black.getPixel(x, y)
                        } else {
                            pixels[y * width + x] = colorBlack
                        }
                    } else {
                        pixels[y * width + x] = colorWhite// 白色色块像素设置
                    }
                }
            }

            /** 4.创建Bitmap对象,根据像素数组设置Bitmap每个像素点的颜色值,并返回Bitmap对象  */
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)

            /** 5.为二维码添加logo图标  */
            return bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
            return null
        }
    }
}