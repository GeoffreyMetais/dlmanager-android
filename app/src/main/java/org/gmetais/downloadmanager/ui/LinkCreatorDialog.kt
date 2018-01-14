package org.gmetais.downloadmanager.ui

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.experimental.CoroutineStart
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.gmetais.downloadmanager.data.SharedFile
import org.gmetais.downloadmanager.databinding.DialogLinkCreatorBinding
import org.gmetais.downloadmanager.getNameFromPath
import org.gmetais.downloadmanager.getRootView
import org.gmetais.downloadmanager.repo.DatabaseRepo
import org.gmetais.downloadmanager.share

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
class LinkCreatorDialog : BottomSheetDialogFragment() {

    private val mPath : String by lazy { arguments?.getString("path") ?: "" }
    private lateinit var mBinding: DialogLinkCreatorBinding

    inner class ClickHandler {
        fun onClick() = addFile()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DialogLinkCreatorBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.title = mPath.getNameFromPath()
        mBinding.handler = ClickHandler()
        mBinding.editName.setOnEditorActionListener { _,_,_ -> addFile(); true }
    }

    private fun addFile() = launch(UI, CoroutineStart.UNDISPATCHED) {
        try {
            val result = DatabaseRepo.add(SharedFile(path = mPath, name = mBinding.editName.text.toString()))
            activity?.let { it.share(result) }
        } catch (e: Exception) {
            activity?.let { Snackbar.make(it.getRootView(), e.message.toString(), Snackbar.LENGTH_LONG).show() }
        } finally {
            dismiss()
        }
    }
}