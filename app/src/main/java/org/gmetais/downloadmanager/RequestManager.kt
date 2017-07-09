@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

package org.gmetais.downloadmanager

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RequestManager {
    private val browserService: IBrowser

    init {
        browserService = Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .client(OkHttpClient.Builder()
                        .addInterceptor(BasicAuthInterceptor(BuildConfig.API_USERNAME, BuildConfig.API_SECRET))
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .readTimeout(5, TimeUnit.SECONDS)
                        .build())
                .addConverterFactory(MoshiConverterFactory.create())
                .build().create(IBrowser::class.java)
    }

    suspend fun browse(path : String?) : Response<Directory> {
        val files = if (path === null) browserService.browseRoot() else browserService.browseDir(RequestBody(path, ""))
        try {
            return files.execute()
        } catch(e: Exception) {
            return Response.error(408, ResponseBody.create(null, e.localizedMessage))
        }
    }

    suspend fun listShares() : Response<MutableList<SharedFile>> {
        try {
            return browserService.getShares().execute()
        } catch(e: Exception) {
            return Response.error(408, ResponseBody.create(null, e.localizedMessage))
        }
    }

    suspend fun add(file: SharedFile) : Response<Void> {
        try {
            return browserService.add(file).execute()
        } catch(e: Exception) {
            return Response.error(408, ResponseBody.create(null, e.localizedMessage))
        }
    }

    suspend fun delete(key: String) : Boolean {
        try {
            return browserService.delete(key).execute().isSuccessful
        } catch(e: Exception) {
            return false
        }
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
