package org.fly.http.request

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/8 11:39 上午
 * @description: 通用的请求Service
 * @since: 1.0.0
 */
interface RequestService {

    /**
     * Http Get
     */
    @GET
    suspend fun get(
        @Url url: String,
        @HeaderMap headers: Map<String, String>,
        @QueryMap params: Map<String, String>
    ): Response<String>

    /**
     * Http Post
     * Data post in form
     */
    @FormUrlEncoded
    @POST
    suspend fun postForm(
        @Url url: String,
        @HeaderMap headers: Map<String, String>,
        @FieldMap params: Map<String, String>
    ): Response<String>

    /**
     * Http Post
     * Data post in json
     */
    @Headers("Content-Type:application/json")
    @POST
    suspend fun postJson(
        @Url url: String,
        @HeaderMap headers: Map<String, String>,
        @Body params: String
    ): Response<String>

    /**
     * Http Post
     * Post a file to remote server
     */
    @POST
    suspend fun postOrigin(
        @Url url: String,
        @HeaderMap headers: Map<String, String>,
        @Body body: RequestBody
    ): Response<String>

    /**
     * Http Put
     */
    @PUT
    suspend fun put(@Url url: String, @HeaderMap headers: Map<String, String>): Response<String>

    /**
     * Http Delete
     */
    @DELETE
    suspend fun delete(@Url url: String, @HeaderMap headers: Map<String, String>): Response<String>

    /**
     * Http Head
     */
    @HEAD
    suspend fun head(@Url url: String, @HeaderMap headers: Map<String, String>): Response<String>

    /**
     * Http Options
     */
    @OPTIONS
    suspend fun options(@Url url: String, @HeaderMap headers: Map<String, String>): Response<String>

    /**
     * Http GET
     * download file
     */
    @Streaming
    @GET
    suspend fun downloadFile(@Url url: String): Response<ResponseBody>
}