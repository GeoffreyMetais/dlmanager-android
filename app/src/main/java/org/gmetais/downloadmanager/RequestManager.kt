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

    fun browseRoot(onSuccess: (Directory) -> Unit, onFailure: (String) -> Unit) {
        val files = browserService.browseRoot()
        files.enqueue(object : Callback<Directory> {
            override fun onResponse(call: Call<Directory>,
                                    response: Response<Directory>) {
                val body : Directory? = response.body()
                if (body != null)
                    onSuccess(body)
                else
                    onFailure("Error reading response")
            }

            override fun onFailure(call: Call<Directory>, t: Throwable) {
                onFailure(t.message ?: "Internal error")
            }
        })
    }
}