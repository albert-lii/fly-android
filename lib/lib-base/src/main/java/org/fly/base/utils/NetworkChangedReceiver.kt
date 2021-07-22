package org.fly.base.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import java.util.*

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/7/21 9:52 下午
 * @description: 网络变化的广播接收器
 * @since: 1.0.0
 */
class NetworkChangedReceiver : BroadcastReceiver() {

    companion object {
        @JvmStatic
        fun getInstance(): NetworkChangedReceiver {
            return SingletonHolder.holder
        }
    }

    private object SingletonHolder {
        val holder = NetworkChangedReceiver()
    }

    private var currentType: NetworkUtils.NetworkType? = null
    private val listeners: HashSet<OnNetworkStatusChangedListener> = HashSet()

    fun registerListener(listener: OnNetworkStatusChangedListener) {
        val preSize: Int = listeners.size
        listeners.add(listener)
        if (preSize == 0 && listeners.size == 1) {
            currentType = NetworkUtils.getNetworkType(ContextUtils.getContext())
            val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            ContextUtils.getApplication().registerReceiver(getInstance(), intentFilter)
        }
    }

    fun unregisterListener(listener: OnNetworkStatusChangedListener) {
        val preSize: Int = listeners.size
        listeners.remove(listener)
        if (preSize == 1 && listeners.size == 0) {
            currentType = null
            ContextUtils.getApplication().unregisterReceiver(getInstance())
        }
    }

    fun isRegistered(listener: OnNetworkStatusChangedListener): Boolean {
        return listeners.contains(listener)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (ConnectivityManager.CONNECTIVITY_ACTION == intent!!.action) {
            val networkType = NetworkUtils.getNetworkType(context!!)
            if (currentType == networkType) {
                // 网络未发生变化
                return
            }
            currentType = networkType
            if (networkType == NetworkUtils.NetworkType.NETWORK_NO) {
                for (listener in listeners) {
                    listener.onDisconnected()
                }
            } else {
                for (listener in listeners) {
                    listener.onConnected(networkType)
                }
            }
        }
    }

    interface OnNetworkStatusChangedListener {
        fun onConnected(networkType: NetworkUtils.NetworkType?)

        fun onDisconnected()
    }
}