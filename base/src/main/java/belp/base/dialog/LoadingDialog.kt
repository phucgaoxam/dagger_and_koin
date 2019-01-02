package belp.base.dialog

import android.content.Context
import android.os.Bundle
import belp.base.R
import belp.base.databinding.DialogLoadingBinding

/**
 * Created by vophamtuananh on 1/7/18.
 */
class LoadingDialog(context: Context) : BaseDialog<DialogLoadingBinding>(context) {

    private var mOnLoadingDialogListener: OnLoadingDialogListener? = null

    override fun getLayoutId(): Int {
        return R.layout.dialog_loading
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(false)
    }

    override fun dismiss() {
        mOnLoadingDialogListener?.onDismissed()
        super.dismiss()
    }

    fun showWithListener(onLoadingDialogListener: OnLoadingDialogListener? = null) {
        mOnLoadingDialogListener = onLoadingDialogListener
        super.show()
    }

    interface OnLoadingDialogListener {
        fun onDismissed()
    }
}