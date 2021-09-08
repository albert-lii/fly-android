package org.fly.base.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/7/28 5:53 下午
 * @description: 图片相关工具类
 * @since: 1.0.0
 */
object ImageUtils {

    /**
     * 更新图库
     */
    @JvmStatic
    fun updateGallery(context: Context, file: File) {
        val intent = Intent()
        intent.action = Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
        intent.data = Uri.fromFile(file)
        context.sendBroadcast(intent)
    }

    /**
     * 保存图片到相册
     */
    @JvmStatic
    fun savePicToGallery(bitmap: Bitmap, fileName: String? = null) {
        val fname = if (fileName.isNullOrEmpty()) {
            "Pic_" + System.currentTimeMillis() + ".jpg"
        } else {
            fileName + ".jpg"
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            insertImageAndroidQ(bitmap, fname)
        } else {
            insertImageLegacy(bitmap, fname)
        }
    }

    @SuppressLint("InlinedApi")
    private fun insertImageAndroidQ(bitmap: Bitmap, fileName: String) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }
        val contentResolver = AppUtils.getContext().contentResolver
        val uri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        try {
            uri?.let {
                val imageOutStream = contentResolver.openOutputStream(it)
                imageOutStream?.use {
                    bitmap.compress(
                        Bitmap.CompressFormat.JPEG,
                        100,
                        imageOutStream
                    )
                }
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                contentResolver.update(uri, contentValues, null, null)
            }
        } catch (e: Exception) {
            if (uri != null) {
                contentResolver.delete(uri, null, null)
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
            }
        }
    }

    private fun insertImageLegacy(bitmap: Bitmap, fileName: String) {
        // 首先保存图片
        val appDir = File(
            Environment.getExternalStorageDirectory()
                .toString() + File.separator + Environment.DIRECTORY_DCIM + File.separator + "Camera" + File.separator
        )
        if (!appDir.exists()) {
            appDir.mkdir()
        }
        val file = File(appDir, fileName)
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            fos?.close()
        }
        //正确修改图片详情显示时间
        val insertImage: String?
        try {
            val context = AppUtils.getContext()
            insertImage = MediaStore.Images.Media.insertImage(
                context.contentResolver,
                file.absolutePath,
                fileName,
                null
            )
            val file1 = File(getRealPathFromURI(context, Uri.parse(insertImage)))
            updateGallery(context, file1)
        } catch (e: FileNotFoundException) {

        }
    }

    /**
     * 得到绝对地址
     */
    @JvmStatic
    fun getRealPathFromURI(context: Context, contentUri: Uri): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = context.contentResolver.query(contentUri, proj, null, null, null)
        if (cursor != null) {
            val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val fileStr: String = cursor.getString(column_index)
            cursor.close()
            return fileStr
        }
        return null
    }

    /**
     * 获取图片旋转角度
     */
    @JvmStatic
    fun getBitmapDegree(path: String): Int {
        var degree = 0
        try {
            val exifInterface = ExifInterface(path)
            val orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
                else -> degree = 0
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return degree
    }

    /**
     * 旋转图片
     */
    fun rotateBitmapByDegree(bm: Bitmap, degree: Int): Bitmap {
        var returnBm: Bitmap? = null
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        try {
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, matrix, true)
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
        }

        if (returnBm == null) {
            returnBm = bm
        }
        if (bm != returnBm) {
            bm.recycle()
        }
        return returnBm
    }
}