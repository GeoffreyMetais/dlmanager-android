package org.gmetais.downloadmanager

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface IBrowser {
    @POST("go/open")
    fun browseDir(@Body request: RequestBody): Call<Directory>

    @GET("go/open")
    fun browseRoot(): Call<Directory>

    @GET("go/list")
    fun getShares(): Call<List<SharedFile>>
}