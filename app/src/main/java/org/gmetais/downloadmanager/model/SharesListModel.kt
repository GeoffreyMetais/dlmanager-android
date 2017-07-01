package org.gmetais.downloadmanager.model

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Handler
import android.os.Looper
import org.gmetais.downloadmanager.RequestManager
import org.gmetais.downloadmanager.SharedFile

class SharesListModel : ViewModel() {
    @Volatile var invalidated = false
    val shares : MutableLiveData<MutableList<SharedFile>> by lazy {
        loadShares()
        MutableLiveData<MutableList<SharedFile>>()
    }

    fun getSharesList() : MutableLiveData<MutableList<SharedFile>> {
        if (invalidated) {
            loadShares()
            invalidated = false
        }
        return shares
    }

    fun loadShares() {
        Thread(Runnable {
            val response = RequestManager.listShares()
            if (response.isSuccessful)
                shares.postValue(response.body())
            invalidated = false
        }).start()
    }

    fun delete(share: SharedFile) {
        Thread(Runnable {
            if (RequestManager.delete(share.name)) {
                shares.value?.let {
                    it.remove(share)
                    shares.postValue(it)
                }
            }
        }).start()
    }

    fun invalidate() {
        invalidated = true
    }
}