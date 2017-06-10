package org.gmetais.downloadmanager

import android.content.Context
import android.widget.Toast
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

    fun browseRoot(ctx : Context) {
        val files = browserService.browseRoot()
        files.enqueue(object : Callback<Directory> {
            override fun onResponse(call: Call<Directory>,
                                    response: Response<Directory>) {
                val directory = response.body()
                if (directory != null) {
                    val filesList = directory.Files
                    val sb: StringBuilder = StringBuilder()
                    for (file in filesList)
                        sb.append(file.Path).append("\n")
                    Toast.makeText(ctx, sb.toString(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Directory>, t: Throwable) {
                Toast.makeText(ctx, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}