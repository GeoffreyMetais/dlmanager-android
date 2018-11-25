package org.gmetais.downloadmanager.ui

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import org.gmetais.downloadmanager.data.SharedFile
import org.gmetais.downloadmanager.databinding.DialogLinkCreatorBinding
import org.gmetais.downloadmanager.getNameFromPath
import org.gmetais.downloadmanager.getRootView
import org.gmetais.downloadmanager.repo.DatabaseRepo
import org.gmetais.downloadmanager.share
import org.gmetais.tools.uiTask

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
class LinkCreatorDialog : BottomSheetDialogFragment() {

    private val path : String by lazy { arguments?.getString("path") ?: "" }
    private lateinit var binding: DialogLinkCreatorBinding
    private val job: Job = Job()

    inner class ClickHandler {
        fun onClick() = addFile()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogLinkCreatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.title = path.getNameFromPath()
        binding.handler = ClickHandler()
        binding.editName.setOnEditorActionListener { _, _, _ -> addFile(); true }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    private fun addFile() = uiTask {
        try {
            if (!isActive) return@uiTask
            val result = DatabaseRepo.add(SharedFile(path = path, name = binding.editName.text.toString()))
            if (isActive) activity?.share(result)
        } catch (e: Exception) {
            activity?.let { Snackbar.make(it.getRootView(), e.message.toString(), Snackbar.LENGTH_LONG).show() }
        } finally {
            dismiss()
        }
    }
}