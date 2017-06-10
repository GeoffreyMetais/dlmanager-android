package org.gmetais.downloadmanager

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RequestManager {
    private val BASE_URL = "http://192.168.1.18:8088/"

    private val browserService: IBrowser
    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        browserService = retrofit.create(IBrowser::class.java)
    }

    fun browseRoot(callback: (Directory?, String?) -> Unit) {
        val files = browserService.browseRoot()
        files.enqueue(object : Callback<Directory> {
            override fun onResponse(call: Call<Directory>,
                                    response: Response<Directory>) {
                callback(response.body(), null)
            }

            override fun onFailure(call: Call<Directory>, t: Throwable) {
                callback(null, t.message)
            }
        })
    }
}