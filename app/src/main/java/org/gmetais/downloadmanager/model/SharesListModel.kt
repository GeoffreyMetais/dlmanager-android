package org.gmetais.downloadmanager.model

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.gmetais.downloadmanager.RequestManager
import org.gmetais.downloadmanager.SharedFile

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
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
        async(CommonPool) {
            with(RequestManager.listShares()) {
                if (isSuccessful)
                    launch(UI) { shares.value = body() }
                invalidated = false
            }
        }
    }

    fun delete(share: SharedFile) {
        async(CommonPool) {
            if (RequestManager.delete(share.name)) {
                shares.value?.let {
                    it.remove(share)
                    launch(UI) {
                        shares.value = it
                    }
                }
            }
        }
    }

    fun invalidate() {
        invalidated = true
    }
}