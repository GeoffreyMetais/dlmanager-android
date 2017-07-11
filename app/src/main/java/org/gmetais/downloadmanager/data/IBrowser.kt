package org.gmetais.downloadmanager.data

import retrofit2.Call
import retrofit2.http.*

interface IBrowser {
    @POST("go/browse")
    fun browseDir(@Body request: RequestBody): Call<Directory>

    @GET("go/browse")
    fun browseRoot(): Call<Directory>

    @GET("go/list")
    fun getShares(): Call<MutableList<SharedFile>>

    @POST("go/add")
    fun add(@Body file: SharedFile) : Call<Void>

    @DELETE("go/del/{name}")
    fun delete(@Path("name") name: String) : Call<Void>
}