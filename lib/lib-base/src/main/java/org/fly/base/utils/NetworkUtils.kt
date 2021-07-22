package org.fly.base.utils

import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.annotation.RequiresPermission
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/7/21 5:21 下午
 * @description: 网络相关工具类
 * @since: 1.0.0
 */
object NetworkUtils {

    enum class NetworkType {
        NETWORK_ETHERNET,
        NETWORK_WIFI,
        NETWORK_5G,
        NETWORK_4G,
        NETWORK_3G,
        NETWORK_2G,
        NETWORK_UNKNOWN,
        NETWORK_NO
    }

    /**
     * 打开wifi设置页面
     */
    @JvmStatic
    fun openWirelessSettings(context: Context) {
        context.startActivity(
            Intent(Settings.ACTION_WIRELESS_SETTINGS)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    /**
     * 获取网络运营商的名称
     */
    @JvmStatic
    fun getNetworkOperatorName(context: Context): String? {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.networkOperatorName
    }

    @JvmStatic
    @RequiresPermission(permission.ACCESS_NETWORK_STATE)
    private fun getActiveNetworkInfo(context: Context): NetworkInfo? {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }

    /**
     * 是否有网络连接
     */
    @JvmStatic
    @RequiresPermission(permission.ACCESS_NETWORK_STATE)
    fun isConnected(context: Context): Boolean {
        val info = getActiveNetworkInfo(context)
        return info != null && info.isConnected
    }

    /**
     * 判断移动数据是否可用
     */
    @JvmStatic
    @SuppressLint("PrivateApi")
    @RequiresPermission(permission.ACCESS_NETWORK_STATE)
    fun getMobileDataEnabled(context: Context): Boolean {
        try {
            val tm =
                context.applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return tm.isDataEnabled
            }
            val getMobileDataEnabledMethod = tm.javaClass.getDeclaredMethod("getDataEnabled")
            if (getMobileDataEnabledMethod != null) {
                return getMobileDataEnabledMethod.invoke(tm) as Boolean
            }
        } catch (e: Exception) {
            LogUtils.e("fly-android ==> NetworkUtis#getMobileDataEnabled: ", e.message ?: "")
        }
        return false
    }

    /**
     * 判断是否开启wifi
     */
    @JvmStatic
    @RequiresPermission("android.permission.ACCESS_WIFI_STATE")
    @SuppressLint("WifiManagerLeak")
    fun getWifiEnabled(context: Context): Boolean {
        val manager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return manager.isWifiEnabled
    }

    /**
     * 设置wifi开启或关闭
     */
    @JvmStatic
    @RequiresPermission(permission.CHANGE_WIFI_STATE)
    @SuppressLint("WifiManagerLeak")
    fun setWifiEnabled(context: Context, enabled: Boolean) {
        val manager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (enabled == manager.isWifiEnabled) return
        manager.isWifiEnabled = enabled
    }

    /**
     * 是否使用的是移动数据
     */
    @JvmStatic
    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    fun isMobileData(context: Context): Boolean {
        val info: NetworkInfo? = getActiveNetworkInfo(context)
        return null != info && info.isAvailable && info.type == 0
    }

    /**
     * 是否正在使用4G网络
     */
    @JvmStatic
    @RequiresPermission(permission.ACCESS_NETWORK_STATE)
    fun is4G(context: Context): Boolean {
        val info = getActiveNetworkInfo(context)
        return (info != null && info.isAvailable
                && info.subtype == TelephonyManager.NETWORK_TYPE_LTE)
    }

    /**
     * 是否正在使用5G网络
     */
    @JvmStatic
    @RequiresPermission(permission.ACCESS_NETWORK_STATE)
    fun is5G(context: Context): Boolean {
        val info = getActiveNetworkInfo(context)
        return (info != null && info.isAvailable
                && info.subtype == TelephonyManager.NETWORK_TYPE_NR)
    }


    /**
     * 是否有连接wifi
     */
    @JvmStatic
    @RequiresPermission(permission.ACCESS_NETWORK_STATE)
    fun isWifiConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = cm.activeNetworkInfo
        return ni != null && ni.type == ConnectivityManager.TYPE_WIFI
    }

    @JvmStatic
    @RequiresPermission(permission.ACCESS_NETWORK_STATE)
    private fun isEthernet(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = cm.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET) ?: return false
        val state = info.state ?: return false
        return state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING
    }

    /**
     * 获取网络类型
     */
    @JvmStatic
    @RequiresPermission(permission.ACCESS_NETWORK_STATE)
    fun getNetworkType(context: Context): NetworkType {
        if (isEthernet(context)) {
            return NetworkType.NETWORK_ETHERNET
        }
        val info = getActiveNetworkInfo(context)
        return if (info != null && info.isAvailable) {
            if (info.type == ConnectivityManager.TYPE_WIFI) {
                NetworkType.NETWORK_WIFI
            } else if (info.type == ConnectivityManager.TYPE_MOBILE) {
                when (info.subtype) {
                    TelephonyManager.NETWORK_TYPE_GSM,
                    TelephonyManager.NETWORK_TYPE_GPRS,
                    TelephonyManager.NETWORK_TYPE_CDMA,
                    TelephonyManager.NETWORK_TYPE_EDGE,
                    TelephonyManager.NETWORK_TYPE_1xRTT,
                    TelephonyManager.NETWORK_TYPE_IDEN -> NetworkType.NETWORK_2G
                    TelephonyManager.NETWORK_TYPE_TD_SCDMA,
                    TelephonyManager.NETWORK_TYPE_EVDO_A,
                    TelephonyManager.NETWORK_TYPE_UMTS,
                    TelephonyManager.NETWORK_TYPE_EVDO_0,
                    TelephonyManager.NETWORK_TYPE_HSDPA,
                    TelephonyManager.NETWORK_TYPE_HSUPA,
                    TelephonyManager.NETWORK_TYPE_HSPA,
                    TelephonyManager.NETWORK_TYPE_EVDO_B,
                    TelephonyManager.NETWORK_TYPE_EHRPD,
                    TelephonyManager.NETWORK_TYPE_HSPAP -> NetworkType.NETWORK_3G
                    TelephonyManager.NETWORK_TYPE_IWLAN,
                    TelephonyManager.NETWORK_TYPE_LTE -> NetworkType.NETWORK_4G
                    TelephonyManager.NETWORK_TYPE_NR -> NetworkType.NETWORK_5G
                    else -> {
                        val subtypeName = info.subtypeName
                        if (subtypeName.equals("TD-SCDMA", ignoreCase = true)
                            || subtypeName.equals("WCDMA", ignoreCase = true)
                            || subtypeName.equals("CDMA2000", ignoreCase = true)
                        ) {
                            NetworkType.NETWORK_3G
                        } else {
                            NetworkType.NETWORK_UNKNOWN
                        }
                    }
                }
            } else {
                NetworkType.NETWORK_UNKNOWN
            }
        } else NetworkType.NETWORK_NO
    }

    /**
     * 获取广播地址
     */
    @JvmStatic
    fun getBroadcastIpAddress(): String? {
        try {
            val nis = NetworkInterface.getNetworkInterfaces()
            val adds = LinkedList<InetAddress>()
            while (nis.hasMoreElements()) {
                val ni = nis.nextElement()
                if (!ni.isUp || ni.isLoopback) continue
                val ias = ni.interfaceAddresses
                var i = 0
                val size = ias.size
                while (i < size) {
                    val ia = ias[i]
                    val broadcast = ia.broadcast
                    if (broadcast != null) {
                        return broadcast.hostAddress
                    }
                    i++
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 获取IP地址
     */
    @JvmStatic
    @RequiresPermission(permission.INTERNET)
    fun getIPAddress(useIPv4: Boolean): String? {
        try {
            val nis = NetworkInterface.getNetworkInterfaces()
            val adds = LinkedList<InetAddress>()
            while (nis.hasMoreElements()) {
                val ni = nis.nextElement()
                // To prevent phone of xiaomi return "10.0.2.15"
                if (!ni.isUp || ni.isLoopback) continue
                val addresses = ni.inetAddresses
                while (addresses.hasMoreElements()) {
                    adds.addFirst(addresses.nextElement())
                }
            }
            for (add in adds) {
                if (!add.isLoopbackAddress) {
                    val hostAddress = add.hostAddress
                    val isIPv4 = hostAddress.indexOf(':') < 0
                    if (useIPv4) {
                        if (isIPv4) return hostAddress
                    } else {
                        if (!isIPv4) {
                            val index = hostAddress.indexOf('%')
                            return if (index < 0) hostAddress.toUpperCase(Locale.ROOT) else hostAddress.substring(
                                0,
                                index
                            ).toUpperCase(Locale.ROOT)
                        }
                    }
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }
        return ""
    }
}