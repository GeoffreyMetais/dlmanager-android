package org.gmetais.downloadmanager

import org.gmetais.downloadmanager.Directory
import org.gmetais.downloadmanager.RequestBody
import org.gmetais.downloadmanager.SharedFile

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface IBrowser {
    @POST("go/browse")
    fun browseDir(@Body request: RequestBody): Call<Directory>

    @GET("go/browse")
    fun browseRoot(): Call<Directory>

    @get:GET
    val shares: Call<List<SharedFile>>
}