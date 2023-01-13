package org.fly.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.*

object DeviceUtils {

    const val KEY_DEVICE_ID = "fly_internal_device_id"
    private var deviceId: String = ""

    /**
     * 是否Root过
     */
    @JvmStatic
    fun isRooted(): Boolean {
        val su = "su"
        val locations = arrayOf(
            "/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
            "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/",
            "/system/sbin/", "/usr/bin/", "/vendor/bin/"
        )
        for (location in locations) {
            if (File(location + su).exists()) {
                return true
            }
        }
        return false
    }

    /**
     * 获取设备的Android版本名称，例如：10（即 Android 10）
     */
    @JvmStatic
    fun getSDKVersionName(): String {
        return Build.VERSION.RELEASE
    }

    /**
     * 获取设备的Android版本号，例如：29（即 SDK 29）
     */
    @JvmStatic
    fun getSDKVersionCode(): Int {
        return Build.VERSION.SDK_INT
    }

    /**
     * 获取设备的Android Id
     */
    @SuppressLint("HardwareIds")
    @JvmStatic
    fun getAndroidID(context: Context): String {
        val id = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        return if ("9774d56d682e549c" == id) "" else id ?: ""
    }

    /**
     * 获取设备唯一标识
     * Device ID -> Android ID -> UUID
     */
    @SuppressLint("MissingPermission")
    @JvmStatic
    fun getDeviceId(context: Context): String {
        if (deviceId.isNotEmpty()) {
            return deviceId
        }
        var uniqueId: String? = QuickStore.getInstance().getString(KEY_DEVICE_ID, null)
        if (!uniqueId.isNullOrEmpty()) {
            deviceId = uniqueId
            return uniqueId
        }
        try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            uniqueId = tm.deviceId
        } catch (e: java.lang.Exception) {
            // do nothing
        }
        if (uniqueId.isNullOrEmpty()) {
            uniqueId = getAndroidID(context)
        }
        if (uniqueId.isNullOrEmpty()) {
            uniqueId = getHardwareInfo() + UUID.randomUUID().toString()
        }
        val realUniqueId = SecurityUtils.md5(uniqueId)
        QuickStore.getInstance().put(KEY_DEVICE_ID, realUniqueId)
        deviceId = realUniqueId
        return realUniqueId
    }

    @JvmStatic
    private fun getHardwareInfo(): String {
        val info = JSONObject()
        try {
            info.put("board", Build.BOARD)
            info.put("brand", Build.BRAND)
            info.put("cpu_abi", Build.CPU_ABI)
            info.put("device", Build.DEVICE)
            info.put("display", Build.DISPLAY)
            info.put("host", Build.HOST)
            info.put("id", Build.ID)
            info.put("manufacturer", Build.MANUFACTURER)
            info.put("model", Build.MODEL)
            info.put("product", Build.PRODUCT)
            info.put("tags", Build.TAGS)
            info.put("type", Build.TYPE)
            info.put("user", Build.USER)
            info.put("serial", Build.SERIAL)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return info.toString()
    }

    /**
     * 获取设备名称
     */
    @JvmStatic
    fun getDeviceName(): String {
        var deviceName = Build.MODEL
        if (deviceName.isNullOrEmpty()) {
            deviceName = try {
                val cls = Class.forName("android.os.SystemProperties")
                val clsObj = cls.newInstance()
                val getName = cls.getDeclaredMethod("get", String::class.java)
                getName.invoke(clsObj, "persist.sys.device_name") as String
            } catch (e: Exception) {
                e.printStackTrace()
                Build.UNKNOWN
            }
        }
        return deviceName
    }

    /**
     * 获取系统语言环境
     */
    @JvmStatic
    fun getSystemLanguage(): String {
        return Locale.getDefault().language
    }

    /**
     * 获取设备当前时区
     */
    @JvmStatic
    fun getTimeZone(): String {
        val tz = TimeZone.getDefault()
        return tz.getDisplayName(false, TimeZone.SHORT)
    }

    /**
     * 获取设备的时区偏差（分钟）
     */
    @JvmStatic
    fun getTimeZoneOffset(): Int {
        val calendar = Calendar.getInstance()
        val offset = -(calendar[Calendar.ZONE_OFFSET] + calendar[Calendar.DST_OFFSET])
        return offset / (60 * 1000)
    }
}