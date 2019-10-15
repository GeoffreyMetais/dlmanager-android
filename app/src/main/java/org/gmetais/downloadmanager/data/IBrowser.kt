package org.gmetais.downloadmanager.data

import retrofit2.Call
import retrofit2.http.*

interface IBrowser {
    @POST("browse")
    suspend fun browseDir(@Body request: RequestBody): Directory

    @GET("browse")
    suspend fun browseRoot(): Directory

    @GET("list")
    suspend fun getShares(): MutableList<SharedFile>

    @POST("add")
    suspend fun add(@Body file: SharedFile) : SharedFile

    @DELETE("del/{name}")
    suspend fun delete(@Path("name") name: String) : Void
}