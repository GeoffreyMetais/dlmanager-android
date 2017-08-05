package org.gmetais.downloadmanager.ui

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.gmetais.downloadmanager.data.SharedFile
import org.gmetais.downloadmanager.databinding.DialogLinkCreatorBinding
import org.gmetais.downloadmanager.getNameFromPath
import org.gmetais.downloadmanager.model.SharesListModel
import org.gmetais.downloadmanager.repo.ApiRepo

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
class LinkCreatorDialog : BottomSheetDialogFragment() {

    val mPath : String by lazy { arguments.getString("path") }
    val shares: SharesListModel by lazy { ViewModelProviders.of(activity).get(SharesListModel::class.java) }
    lateinit var mBinding: DialogLinkCreatorBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DialogLinkCreatorBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.title = mPath.getNameFromPath()
        mBinding.handler = ClickHandler()
        mBinding.editName.setOnEditorActionListener { _,_,_ -> addFile(); true }
    }

    inner class ClickHandler {
        fun onClick() = addFile()
    }

    private fun addFile() = launch(UI) {
        if (ApiRepo.add(SharedFile(path = mPath, name = mBinding.editName.text.toString()))) {
            shares.loadData()
            dismiss()
        } else
            Snackbar.make(mBinding.root, "failure", Snackbar.LENGTH_LONG).show()
    }
}