package org.fly.http.ssl

import android.content.Context
import com.cloud.lib.http.ssl.DomainInfo
import com.getkeepsafe.relinker.ReLinker
import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV
import org.fly.http.gson.GsonParser

/**
 * 证书信息缓存
 */
class CertificateStore {
    companion object {

        @Volatile
        private var instance: CertificateStore? = null

        @JvmStatic
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: CertificateStore().also { instance = it }
            }

        @JvmStatic
        fun initialize(context: Context) {
            // 一些 Android 设备（API level 19）在安装/更新 APK 时可能出错, 导致 libmmkv.so 找不到
            MMKV.initialize(context) { libName -> ReLinker.loadLibrary(context, libName) }
        }

        @JvmStatic
        fun initialize(context: Context, rootDir: String) {
            // 一些 Android 设备（API level 19）在安装/更新 APK 时可能出错, 导致 libmmkv.so 找不到
            MMKV.initialize(context, rootDir) { libName -> ReLinker.loadLibrary(context, libName) }
        }
    }

    private val cache: MMKV? = MMKV.mmkvWithID("Fly-Http-SSL", 1, "Fly-Http-SSL-Crypt")
    private val gson = GsonParser.getGson()
    private var defaultDomainList = mutableListOf<DomainInfo>() // 默认的域名信息列表
    private var isUpdateFromRemote = false // 是否从远程更新了最新的证书列表

    /**
     * 是否从远程更新了域名证书信息
     */
    fun isUpdateFromRemote(): Boolean {
        return isUpdateFromRemote
    }

    /**
     * 设置默认的域名信息
     */
    fun setDefaultDomainList(list: MutableList<DomainInfo>) {
        this.defaultDomainList = list
    }

    /**
     * 保存DomainInfo列表
     */
    fun saveDomainList(list: MutableList<DomainInfo>) {
        cache?.clearAll()
        if (list.isNotEmpty()) {
            list.forEach {
                saveDomain(it.domain, it.key, it.name)
            }
        }
        isUpdateFromRemote = true
    }

    /**
     * 保存DomainInfo
     */
    fun saveDomain(hostname: String, encryptedHash: String, certCN: String) {
        val realHash = EncryptUtil.decryptWithAES(encryptedHash) ?: ""
        saveCert(hostname, realHash, certCN)
    }

    /**
     * 保存证书信息
     */
    fun saveCert(hostname: String, hash: String, certCN: String) {
        cache?.encode(hostname, gson.toJson(mapOf("hash" to hash, "cn" to certCN)))
    }

    /**
     * 获取证书信息
     */
    fun getCert(hostname: String): Map<String, String>? {
        val json = cache?.decodeString(hostname)
        if (json.isNullOrEmpty()) {
            return null
        }
        return gson.fromJson<Map<String, String>>(
            json,
            object : TypeToken<Map<String, String>>() {}.type
        )
    }

    /**
     * 获取所有的证书列表
     */
    fun getAll(): List<CertificateDigest> {
        val certs = mutableSetOf<CertificateDigest>()
        val keys = cache?.allKeys() ?: emptyArray()
        // 获取本地缓存的所有证书信息
        keys.forEach { host ->
            val map = getCert(host) ?: mapOf()
            certs.add(CertificateDigest(host, map["hash"] ?: "", map["cn"] ?: ""))
        }
        if (!isUpdateFromRemote) {
            // 如果没有从远程更新域名证书信息，需要检查本地默认的域名信息列表中是否存在本地缓存中所没有的域名信息
            // 如果存在，需要添加到域名信息列表中
            val defaultDomains = defaultDomainList
            defaultDomains.forEach { info ->
                var isExisted = false
                for (cert in certs) {
                    if (cert.hostname == info.domain) {
                        isExisted = true
                        break
                    }
                }
                if (!isExisted) {
                    certs.add(CertificateDigest(info.domain, info.key, info.name))
                }
            }
        }
        if (certs.isEmpty()) {
            // 如果获取的域名信息列表依旧为空，则将本地默认的域名信息作为当前可用的域名证书信息列表
            val defaultDomains = defaultDomainList
            defaultDomains.forEach { info ->
                certs.add(CertificateDigest(info.domain, info.key, info.name))
            }
        }
        return certs.toList()
    }
}