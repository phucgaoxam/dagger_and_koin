/*
 * Copyright (c) 2019.
 * This file was created by Phuc Chau on 1/17/19 3:58 PM
 */

package belp.base.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class BaseDialogFragment : DialogFragment() {

    companion object {
        const val DIALOG_EXTRA = "mDialog"

        fun newInstance(dialog: BaseDialog<*>): BaseDialogFragment {
            val baseDialogFragment = BaseDialogFragment()
            val bundle = Bundle()
            bundle.putSerializable(DIALOG_EXTRA, dialog)
            baseDialogFragment.arguments = bundle
            return baseDialogFragment
        }
    }

    private var mDialog: BaseDialog<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDialog = arguments?.getSerializable(DIALOG_EXTRA) as BaseDialog<*>
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return mDialog!!
    }
}

