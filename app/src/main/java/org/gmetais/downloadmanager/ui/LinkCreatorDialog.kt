package org.gmetais.downloadmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import org.gmetais.downloadmanager.data.SharedFile
import org.gmetais.downloadmanager.databinding.DialogLinkCreatorBinding
import org.gmetais.downloadmanager.getNameFromPath
import org.gmetais.downloadmanager.repo.DataRepo
import org.gmetais.downloadmanager.share

@ExperimentalCoroutinesApi
@Suppress("EXPERIMENTAL_FEATURE_WARNING")
class LinkCreatorDialog : BottomSheetDialogFragment(), CoroutineScope by MainScope() {

    private val path : String by lazy { arguments?.getString("path") ?: "" }
    private lateinit var binding: DialogLinkCreatorBinding

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
        binding.editName.setOnEditorActionListener { _, _, _ -> addFile() }
    }

    override fun onDestroy() {
        coroutineContext.cancel()
        super.onDestroy()
    }

    private fun addFile() : Boolean {
        launch {
            if (isActive) try {
                val result = DataRepo.add(SharedFile(path = path, name = binding.editName.text.toString()))
                if (isActive) activity?.share(result)
            } catch (e: Exception) {
                activity?.run { Snackbar.make(binding.root, e.message.toString(), Snackbar.LENGTH_LONG).show() }
            } finally {
                dismiss()
            }
        }
        return true
    }
}
