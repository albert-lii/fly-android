package org.fly.http.request

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 * 通用的请求Service
 *
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/8 11:39 上午
 * @since: 1.0.0
 */
interface RequestService {

    /**
     * Get请求
     */
    @GET
    suspend fun get(
        @Url url: String,
        @HeaderMap headers: Map<String, String>,
        @QueryMap params: Map<String, String>
    ): Response<String>

    /**
     * Post请求，提交表单数据
     */
    @FormUrlEncoded
    @POST
    suspend fun postForm(
        @Url url: String,
        @HeaderMap headers: Map<String, String>,
        @FieldMap params: Map<String, String>
    ): Response<String>

    /**
     * Post请求，提交Json数据
     */
    @Headers("Content-Type:application/json")
    @POST
    suspend fun postJson(
        @Url url: String,
        @HeaderMap headers: Map<String, String>,
        @Body params: String
    ): Response<String>

    /**
     * Post请求，上传文件
     */
    @POST
    suspend fun postOrigin(
        @Url url: String,
        @HeaderMap headers: Map<String, String>,
        @Body body: RequestBody
    ): Response<String>

    /**
     * Put请求
     */
    @PUT
    suspend fun put(@Url url: String, @HeaderMap headers: Map<String, String>): Response<String>

    /**
     * Delete请求
     */
    @DELETE
    suspend fun delete(@Url url: String, @HeaderMap headers: Map<String, String>): Response<String>

    /**
     * Head请求
     */
    @HEAD
    suspend fun head(@Url url: String, @HeaderMap headers: Map<String, String>): Response<String>

    /**
     * Options请求
     */
    @OPTIONS
    suspend fun options(@Url url: String, @HeaderMap headers: Map<String, String>): Response<String>

    /**
     * Get请求，下载文件
     */
    @Streaming
    @GET
    suspend fun downloadFile(@Url url: String): Response<ResponseBody>
}