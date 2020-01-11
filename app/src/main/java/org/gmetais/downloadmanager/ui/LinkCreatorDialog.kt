package org.gmetais.downloadmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.gmetais.downloadmanager.data.SharedFile
import org.gmetais.downloadmanager.databinding.DialogLinkCreatorBinding
import org.gmetais.downloadmanager.getNameFromPath
import org.gmetais.downloadmanager.repo.DataRepo
import org.gmetais.downloadmanager.share

@ExperimentalCoroutinesApi
@Suppress("EXPERIMENTAL_FEATURE_WARNING")
class LinkCreatorDialog : BottomSheetDialogFragment() {

    private val path : String by lazy { arguments?.getString("path") ?: "" }
    private lateinit var binding: DialogLinkCreatorBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogLinkCreatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dialogTitle.text = path.getNameFromPath()
        binding.root.setOnClickListener { addFile() }
        binding.editName.setOnEditorActionListener { _, _, _ -> addFile() }
    }

    private fun addFile() : Boolean {
        lifecycleScope.launchWhenStarted {
            try {
                val result = DataRepo.add(SharedFile(path = path, name = binding.editName.text.toString()))
                activity?.share(result)
            } catch (e: Exception) {
                activity?.run { Snackbar.make(binding.root, e.message.toString(), Snackbar.LENGTH_LONG).show() }
            } finally {
                dismiss()
            }
        }
        return true
    }
}
