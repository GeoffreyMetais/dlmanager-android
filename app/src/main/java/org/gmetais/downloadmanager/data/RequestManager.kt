@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

package org.gmetais.downloadmanager.data

import android.preference.PreferenceManager
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.gmetais.downloadmanager.Application
import org.gmetais.downloadmanager.BuildConfig
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RequestManager {
    private val browserService: IBrowser

    fun browse(path : String?) : Response<Directory> = (if (path === null) browserService.browseRoot() else browserService.browseDir(RequestBody(path, ""))).execute()

    fun listShares(): Response<MutableList<SharedFile>> = browserService.getShares().execute()

    fun add(file: SharedFile) = browserService.add(file).execute().isSuccessful

    fun delete(key: String) = browserService.delete(key).execute().isSuccessful

    init {
        val pm = PreferenceManager.getDefaultSharedPreferences(Application.getContext())
        browserService = Retrofit.Builder()
                .baseUrl(pm.getString("server_url", BuildConfig.API_URL))
                .client(OkHttpClient.Builder()
                        .addInterceptor(BasicAuthInterceptor(pm.getString("username", BuildConfig.API_USERNAME), pm.getString("password", BuildConfig.API_SECRET)))
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .readTimeout(5, TimeUnit.SECONDS)
                        .build())
                .addConverterFactory(MoshiConverterFactory.create())
                .build().create(IBrowser::class.java)
    }

    private class BasicAuthInterceptor(val username : String, val passw : String) : Interceptor {
        val credentials : String by lazy { Credentials.basic(username, passw) }
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val request = chain.request()
            val authenticatedRequest = request.newBuilder()
                    .header("Authorization", credentials).build()
            return chain.proceed(authenticatedRequest)
        }

    }
}
