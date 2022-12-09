package com.cloud.lib.http.ssl

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * 服务端返回的域名信息
 */
data class DomainInfo(
    @Expose
    @SerializedName("domain")
    var domain: String = "",
    @Expose
    @SerializedName("key")
    var key: String = "",
    @Expose
    @SerializedName("name")
    var name: String = "",
    @Expose
    @SerializedName("webviewUrl")
    var webviewUrl: String = "",
    @Expose
    @SerializedName("wssUrl")
    var wssUrl: String = ""
)