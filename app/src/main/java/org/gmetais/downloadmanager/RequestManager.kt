@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

package org.gmetais.downloadmanager

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
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
                .client(OkHttpClient.Builder()
                        .addInterceptor(BasicAuthInterceptor("dekans", "password"))
                        .build())
                .addConverterFactory(MoshiConverterFactory.create())
                .build().create(IBrowser::class.java)
    }

    suspend fun browse(path : String?) : Response<Directory> {
        val files = if (path === null) browserService.browseRoot() else browserService.browseDir(RequestBody(path, ""))
        return files.execute()
    }

    suspend fun listShares() : Response<MutableList<SharedFile>> {
        return browserService.getShares().execute()
    }

    suspend fun add(file: SharedFile) : Response<Void> {
        return browserService.add(file).execute()
    }

    suspend fun delete(key: String) : Boolean {
        return browserService.delete(key).execute().isSuccessful
    }

    class BasicAuthInterceptor(val username : String, val passw : String) : Interceptor {
        val credentials : String by lazy { Credentials.basic(username, passw) }
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val request = chain.request()
            val authenticatedRequest = request.newBuilder()
                    .header("Authorization", credentials).build()
            return chain.proceed(authenticatedRequest)
        }

    }
}
