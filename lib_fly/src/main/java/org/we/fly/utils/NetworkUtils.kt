package org.we.fly.utils

import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
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
 * @time: 2020/6/10 2:02 PM
 * @description: 网络相关工具类
 * @since: 1.0.0
 */

/**
 * 网络类型
 */
enum class NetworkType(private val desc: String) {
    NETWORK_WIFI("WiFi"),
    NETWORK_5G("4G"),
    NETWORK_4G("4G"),
    NETWORK_3G("3G"),
    NETWORK_2G("2G"),
    NETWORK_UNKNOWN("Unknown"),
    NETWORK_NO("No network");

    override fun toString(): String {
        return desc
    }
}

object NetworkUtils {

    /**
     * 打开无线网设置
     */
    fun openWirelessSettings(context: Context) {
        context.startActivity(
            Intent(Settings.ACTION_WIRELESS_SETTINGS).setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
            )
        )
    }

    /**
     * 是否已经连接网络
     */
    @RequiresPermission(permission.ACCESS_NETWORK_STATE)
    fun isConnected(context: Context): Boolean {
        val info = getActiveNetworkInfo(context)
        return info != null && info.isConnected
    }

    /**
     * 获取网络类型
     */
    @RequiresPermission(permission.ACCESS_NETWORK_STATE)
    fun getNetworkType(context: Context): NetworkType {
        var networkType = NetworkType.NETWORK_NO
        val info = getActiveNetworkInfo(context);
        if (info != null && info.isAvailable()) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                networkType = NetworkType.NETWORK_WIFI;
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                when (info.getSubtype()) {
                    TelephonyManager.NETWORK_TYPE_GSM,
                    TelephonyManager.NETWORK_TYPE_GPRS,
                    TelephonyManager.NETWORK_TYPE_CDMA,
                    TelephonyManager.NETWORK_TYPE_EDGE,
                    TelephonyManager.NETWORK_TYPE_1xRTT,
                    TelephonyManager.NETWORK_TYPE_IDEN -> networkType = NetworkType.NETWORK_2G

                    TelephonyManager.NETWORK_TYPE_TD_SCDMA,
                    TelephonyManager.NETWORK_TYPE_EVDO_A,
                    TelephonyManager.NETWORK_TYPE_UMTS,
                    TelephonyManager.NETWORK_TYPE_EVDO_0,
                    TelephonyManager.NETWORK_TYPE_HSDPA,
                    TelephonyManager.NETWORK_TYPE_HSUPA,
                    TelephonyManager.NETWORK_TYPE_HSPA,
                    TelephonyManager.NETWORK_TYPE_EVDO_B,
                    TelephonyManager.NETWORK_TYPE_EHRPD,
                    TelephonyManager.NETWORK_TYPE_HSPAP -> networkType = NetworkType.NETWORK_3G

                    TelephonyManager.NETWORK_TYPE_IWLAN,
                    TelephonyManager.NETWORK_TYPE_LTE -> networkType = NetworkType.NETWORK_4G

                    TelephonyManager.NETWORK_TYPE_NR -> networkType = NetworkType.NETWORK_5G

                    else -> {
                        val subtypeName: String = info.getSubtypeName()
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
                networkType = NetworkType.NETWORK_UNKNOWN;
            }
        }
        return networkType;
    }

    /**
     * 获取ip地址
     */
    @RequiresPermission(permission.INTERNET)
    fun getIPAddress(useIPv4: Boolean): String {
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
                            return if (index < 0) hostAddress.toUpperCase() else hostAddress.substring(
                                0,
                                index
                            ).toUpperCase()
                        }
                    }
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 获取网络信息
     */
    @RequiresPermission(permission.ACCESS_NETWORK_STATE)
    private fun getActiveNetworkInfo(context: Context): NetworkInfo? {
        val cm: Any? = context.getSystemService(Context.CONNECTIVITY_SERVICE)
        if (cm == null) {
            return null
        } else {
            return (cm as ConnectivityManager).activeNetworkInfo
        }
    }

    /**
     * 注册网络变化监听
     */
    fun registerNetworkStateChangedListener(
        context: Context,
        listener: OnNetworkStateChangedListener
    ) {
        NetworkStateChangedReceiver.INSTANCE.registerListener(context, listener)
    }

    /**
     * 解除网络变化监听
     */
    fun unregisterNetworkStateChangedListener(
        context: Context,
        listener: OnNetworkStateChangedListener
    ) {
        NetworkStateChangedReceiver.INSTANCE.unregisterListener(context, listener)
    }

    /**
     * 是否已经注册了网络变化监听
     */
    fun isRegisteredNetworkStateChangedListener(listener: OnNetworkStateChangedListener): Boolean {
        return NetworkStateChangedReceiver.INSTANCE.isRegistered(listener)
    }
}

/**
 * 网络变化监听广播
 */
class NetworkStateChangedReceiver : BroadcastReceiver() {

    private object SingletonHolder {
        val HOLDER = NetworkStateChangedReceiver()
    }

    companion object {
        @JvmStatic
        val INSTANCE = SingletonHolder.HOLDER
    }

    private val listenerSet = HashSet<OnNetworkStateChangedListener>()
    private var currentType = NetworkType.NETWORK_NO

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {
        if (ConnectivityManager.CONNECTIVITY_ACTION == intent!!.action) {
            val networkType = NetworkUtils.getNetworkType(context!!)
            notifyChanged(networkType)
        }
    }

    private fun notifyChanged(networkType: NetworkType) {
        if (currentType == networkType) {
            return
        }
        currentType = networkType;
        if (networkType == NetworkType.NETWORK_NO) {
            for (listener in listenerSet) {
                listener.onDisconnected()
            }
        } else {
            for (listener in listenerSet) {
                listener.onConnected(networkType)
            }
        }
    }

    private fun registerRecevier(context: Context) {
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(INSTANCE, intentFilter)
    }

    private fun unregisterRecevier(context: Context) {
        context.unregisterReceiver(INSTANCE)
    }

    fun isRegistered(listener: OnNetworkStateChangedListener): Boolean {
        return listenerSet.contains(listener)
    }

    @SuppressLint("MissingPermission")
    fun registerListener(context: Context, listener: OnNetworkStateChangedListener) {
        val preSize = listenerSet.size
        listenerSet.add(listener)
        if (preSize == 0 && listenerSet.size == 1) {
            currentType = NetworkUtils.getNetworkType(context)
            registerRecevier(context)
        }
    }

    fun unregisterListener(context: Context, listener: OnNetworkStateChangedListener) {
        val preSize = listenerSet.size
        listenerSet.remove(listener)
        if (preSize == 1 && listenerSet.size == 0) {
            unregisterRecevier(context)
        }
    }
}

interface OnNetworkStateChangedListener {
    fun onConnected(networkType: NetworkType)

    fun onDisconnected()
}
