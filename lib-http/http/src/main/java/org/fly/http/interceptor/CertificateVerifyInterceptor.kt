package org.fly.http.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import org.fly.http.exception.CertVerifyException
import org.fly.http.ssl.CertificatePinnerProxy

/**
 * 证书校验拦截器
 */
class CertificateVerifyInterceptor (
    private val certificatePinnerProxy: CertificatePinnerProxy
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        try {
            val url = chain.request().url.toString()
            if (!certificatePinnerProxy.shouldCheck(url)) {
                return response
            }
            val peerCertificates = response.handshake?.peerCertificates
            if (peerCertificates != null && peerCertificates.isNotEmpty()) {
                /**
                 * {handleShake.peerCertificates()} is an ordered array of peer certificates,
                 * with the peer's own certificate first followed by any
                 * certificate authorities.
                 * @see javax.net.ssl.SSLSession.getPeerCertificates
                 * */
                val peersOwnCertificate = peerCertificates.first()
                val host = response.request.header("Host") ?: return response

                if (certificatePinnerProxy.check(host, url, peersOwnCertificate)) {
                    return response
                }
            }
        } catch (e: Exception) {
            if (e is CertVerifyException) {
                throw e
            }
        }
        return response
    }
}