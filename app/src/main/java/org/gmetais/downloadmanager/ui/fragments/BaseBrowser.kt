package org.gmetais.downloadmanager.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import org.gmetais.downloadmanager.databinding.BrowserBinding

abstract class BaseBrowser : Fragment(), androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {

    abstract override fun onRefresh()

    protected lateinit var binding : BrowserBinding
    private var progressJob : Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = BrowserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.swiperefresh.setOnRefreshListener(this)
    }

    fun showProgress(show: Boolean = true) {
        if (show) {
            progressJob = lifecycleScope.launchWhenStarted {
                delay(300L)
                binding.swiperefresh.isRefreshing = true
            }
        } else {
            progressJob?.cancel()
            binding.swiperefresh.isRefreshing = false
        }
    }

    protected fun onError(exception: Exception?) {
        showProgress(false)
        exception?.let { Snackbar.make(binding.filesList, it.message ?: it.localizedMessage, Snackbar.LENGTH_LONG).show() }
    }
}
