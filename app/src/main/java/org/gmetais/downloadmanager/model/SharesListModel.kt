package org.gmetais.downloadmanager.model

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.gmetais.downloadmanager.RequestManager
import org.gmetais.downloadmanager.SharedFile

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
class SharesListModel : ViewModel() {

    val shares : MutableLiveData<MutableList<SharedFile>> by lazy {
        loadShares()
        MutableLiveData<MutableList<SharedFile>>()
    }

    fun loadShares() {
        async(CommonPool) {
            with(RequestManager.listShares()) {
                if (isSuccessful)
                    shares.postValue(body())
            }
        }
    }

    fun add(share: SharedFile) {
        async(CommonPool) {
            shares.value?.let {
                it.add(share)
                shares.postValue(it)
            }
        }
    }

    fun delete(share: SharedFile) {
        async(CommonPool) {
            if (RequestManager.delete(share.name)) {
                shares.value?.let {
                    it.remove(share)
                    shares.postValue(it)
                }
            }
        }
    }
}