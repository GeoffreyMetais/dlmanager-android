package org.gmetais.downloadmanager.model

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.gmetais.downloadmanager.RequestManager
import org.gmetais.downloadmanager.SharedFile

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
class SharesListModel : ViewModel() {

    val shares : MutableLiveData<Response> by lazy {
        loadShares()
        MutableLiveData<Response>()
    }

    fun loadShares() {
        async(CommonPool) {
            with(RequestManager.listShares()) {
                shares.postValue(if (isSuccessful) Response.Success(body()!!) else Response.Error(code(), message()))
            }
        }
    }

    fun add(share: SharedFile) {
        async(CommonPool) {
            (shares.value as? Response.Success)?.let {
                it.shares.add(share)
                shares.postValue(it)
            }
        }
    }

    fun delete(share: SharedFile) {
        async(CommonPool) {
            if (RequestManager.delete(share.name)) {
                (shares.value as? Response.Success)?.let {
                    it.shares.remove(share)
                    shares.postValue(it)
                }
            }
        }
    }

    sealed class Response {
        data class Success(val shares: MutableList<SharedFile>) : Response()
        data class Error(val code: Int, val message: String) : Response()
    }
}