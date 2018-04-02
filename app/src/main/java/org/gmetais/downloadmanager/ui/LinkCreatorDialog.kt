package org.gmetais.downloadmanager.ui

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.experimental.CoroutineStart
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor
import kotlinx.coroutines.experimental.launch
import org.gmetais.downloadmanager.data.SharedFile
import org.gmetais.downloadmanager.databinding.DialogLinkCreatorBinding
import org.gmetais.downloadmanager.getNameFromPath
import org.gmetais.downloadmanager.getRootView
import org.gmetais.downloadmanager.repo.DatabaseRepo
import org.gmetais.downloadmanager.share

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
class LinkCreatorDialog : BottomSheetDialogFragment() {

    private val path : String by lazy { arguments?.getString("path") ?: "" }
    private lateinit var binding: DialogLinkCreatorBinding
    private val job: Job = Job()
    private val eventActor = actor<Unit>(UI, capacity = Channel.CONFLATED, parent = job) { for (event in channel) addFile() }

    inner class ClickHandler {
        fun onClick() = eventActor.offer(Unit)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogLinkCreatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.title = path.getNameFromPath()
        binding.handler = ClickHandler()
        binding.editName.setOnEditorActionListener { _, _, _ -> eventActor.offer(Unit); true }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    private fun addFile() = launch(UI, CoroutineStart.UNDISPATCHED) {
        try {
            if (!isActive) return@launch
            val result = DatabaseRepo.add(SharedFile(path = path, name = binding.editName.text.toString()))
            if (isActive) activity?.share(result)
        } catch (e: Exception) {
            activity?.let { Snackbar.make(it.getRootView(), e.message.toString(), Snackbar.LENGTH_LONG).show() }
        } finally {
            dismiss()
        }
    }
}