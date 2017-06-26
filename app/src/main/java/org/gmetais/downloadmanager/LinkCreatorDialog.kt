package org.gmetais.downloadmanager

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class LinkCreatorDialog : BottomSheetDialogFragment(), View.OnClickListener {

    val mTitle by bind<TextView>(R.id.title)
    val mButton by bind<Button>(R.id.link_action)
    val mEditText by bind<EditText>(R.id.edit_name)
    lateinit var mPath : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_link_creator, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPath = arguments.getString("path")
        mTitle?.text = getString(R.string.create_download_link_for, mPath.getNameFromPath())
        mButton?.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        RequestManager.add(SharedFile(path = mPath, name = mEditText?.text.toString()), this::onAddResponse)
    }

    fun onAddResponse(success: Boolean) {
        if (success)
            dismiss()
        else
            Snackbar.make(view!!, "failure", Snackbar.LENGTH_LONG).show()
    }
}