package org.gmetais.downloadmanager

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RequestManager {
    private val BASE_URL = "http://gmetais.netlib.re/"
    private val browserService: IBrowser

    init {
        browserService = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build().create(IBrowser::class.java)
    }

    fun browse(path : String?, onSuccess: (Directory) -> Unit, onFailure: (String) -> Unit) {
        val files = if (path === null) browserService.browseRoot() else browserService.browseDir(RequestBody(path, ""))
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

    fun listShares() : Response<MutableList<SharedFile>> {
        return browserService.getShares().execute()
    }

    fun add(file: SharedFile, onResponse: (Boolean) -> Unit) {
        browserService.add(file).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>,
                                    response: Response<Void>) {
                onResponse(response.isSuccessful)
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                onResponse(false)
            }
        })
    }

    fun delete(key: String) : Boolean {
        return browserService.delete(key).execute().isSuccessful
    }
}
