package org.gmetais.downloadmanager

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.gmetais.downloadmanager.databinding.DialogLinkCreatorBinding
import org.gmetais.downloadmanager.model.SharesListModel

class LinkCreatorDialog : BottomSheetDialogFragment() {

    val mPath : String by lazy {arguments.getString("path")}
    val shares: SharesListModel by lazy { ViewModelProviders.of(activity).get(SharesListModel::class.java) }
    lateinit var mBinding: DialogLinkCreatorBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DialogLinkCreatorBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.title = mPath.getNameFromPath()
        mBinding.hanler = ClickHandler()
    }

    inner class ClickHandler {
        fun onClick(view: View?) {
            val file = SharedFile(path = mPath, name = mBinding.editName.text.toString())
            async(CommonPool) {
                if (RequestManager.add(file).isSuccessful) {
                    shares.add(file)
                    dismiss()
                } else
                    Snackbar.make(view!!, "failure", Snackbar.LENGTH_LONG).show()
            }
        }
    }
}