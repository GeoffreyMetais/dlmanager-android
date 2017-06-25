package org.gmetais.downloadmanager

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface IBrowser {
    @POST("go/browse")
    fun browseDir(@Body request: RequestBody): Call<Directory>

    @GET("go/browse")
    fun browseRoot(): Call<Directory>

    @GET("go/list")
    fun getShares(): Call<List<SharedFile>>

    @POST("go/add")
    fun add(@Body file: SharedFile) : Call<Void>
}