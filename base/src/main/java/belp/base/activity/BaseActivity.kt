package belp.base.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import belp.base.R
import belp.base.dialog.ConfirmDialog
import belp.base.dialog.InformDialog
import belp.base.dialog.LoadingDialog
import com.base.exception.ManuallyException
import com.base.exception.NoConnectionException
import com.base.utils.DeviceUtil
import com.base.utils.DeviceUtil.Companion.CAMERA_REQUEST_CODE
import com.base.utils.DeviceUtil.Companion.GALLERY_REQUEST_CODE
import com.base.utils.DeviceUtil.Companion.PERMISSION_CALL_PHONE_REQUEST_CODE
import com.base.utils.DeviceUtil.Companion.PERMISSION_CAMERA_REQUEST_CODE
import com.base.utils.DeviceUtil.Companion.PERMISSION_LOCATION_REQUEST_CODE
import com.base.utils.DeviceUtil.Companion.PERMISSION_READ_EXTERNAL_REQUEST_CODE
import com.base.utils.DeviceUtil.Companion.PERMISSION_WRITE_STORAGE_REQUEST_CODE
import belp.base.viewmodel.ActivityViewModel
import belp.base.viewmodel.CommonView
import retrofit2.HttpException

/**
 * Created by vophamtuananh on 1/7/18.
 */
abstract class BaseActivity<B : ViewDataBinding, VM : ActivityViewModel> : AppCompatActivity(), CommonView {

    protected lateinit var mViewDataBinding: B

    protected var mViewModel: VM? = null

    private var mCapturedPath: String? = null

    private var mCurrentPhoneNumber: String? = null

    private var mLoadingDialog: LoadingDialog? = null

    private var mInformDialog: InformDialog? = null

    private var mConfirmDialog: ConfirmDialog? = null

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    abstract fun getViewModelClass(): Class<VM>?

    override fun onCreate(savedInstanceState: Bundle?) {
        if (showFullScreen()) {
            makeFullscreen()
        }

        super.onCreate(savedInstanceState)
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        if (getViewModelClass() != null)
            mViewModel = ViewModelProviders.of(this).get(getViewModelClass()!!)
        mViewModel?.onAttached(this)
    }

    override fun onPause() {
        super.onPause()
        mLoadingDialog?.dismiss()
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mViewModel?.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        mViewModel?.onRestoreInstanceState(savedInstanceState)
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onDestroy() {
        mViewDataBinding.unbind()
        super.onDestroy()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v != null && v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
            if (showFullScreen())
                makeFullscreen()
        }
        return try {
            super.dispatchTouchEvent(event)
        } catch (e: Exception) {
            true
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_CAMERA_REQUEST_CODE) {
            if (grantResults.size > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                openCamera()
            } else {
                onRejectedCameraPermission()
            }
        } else if (requestCode == PERMISSION_READ_EXTERNAL_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                onRejectedReadExternalPermission()
            }
        } else if (requestCode == PERMISSION_CALL_PHONE_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mCurrentPhoneNumber?.let { callToPhoneNumber(it) }
            } else {
                onRejectedPhoneCallPermission()
            }
        } else if (requestCode == PERMISSION_WRITE_STORAGE_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onAgreedWriteExternal()
            } else {
                onRejectedWriteExternalPermission()
            }
        } else if (requestCode == PERMISSION_LOCATION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onAgreedLocationPermission()
            } else {
                onRejectedLocationPermission()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                if (mCapturedPath != null)
                    onCapturedImage(mCapturedPath!!)
            } else if (requestCode == GALLERY_REQUEST_CODE) {
                if (data != null) {
                    val selectedImage = data.data
                    onChoseImage(selectedImage)
                }
            }
        } else if (requestCode == GALLERY_REQUEST_CODE) {
            onChoseNoImage()
        }
    }

    override fun showLoading() {
        showLoadingDialog()
    }

    override fun hideLoading() {
        hideLoadingDialog()
    }

    override fun showError(throwable: Throwable, tag: String) {
        mInformDialog = getInformDialog()
        if (!mInformDialog!!.isShowing) {
            if (throwable is NoConnectionException) {
                mInformDialog?.show(
                    onConfirmListener = object : InformDialog.OnConfirmListener {
                        override fun onConfirmed(tag: String?) {
                            if (tag != null)
                                onTryAgain(tag)
                        }
                    },
                    tag = tag,
                    informType = InformDialog.InformType.WARNING,
                    description = getThrowableMessage(throwable),
                    buttonText = getString(
                        R.string.try_again
                    )
                )
            } else {
                mInformDialog?.show(
                    informType = InformDialog.InformType.WARNING,
                    description = getThrowableMessage(throwable)
                )
            }
        }
    }

    override fun showError(throwable: Throwable) {
        mInformDialog = getInformDialog()
        if (!mInformDialog!!.isShowing) {
            mInformDialog?.show(
                informType = InformDialog.InformType.WARNING,
                description = getThrowableMessage(throwable)
            )
        }
    }

    private fun getThrowableMessage(e: Throwable): String {
        var msg: String? = null
        if (e is NoConnectionException || e is ManuallyException)
            msg = e.message
        else if (e is HttpException) {
            msg = getHttpExceptionMessage(e)
        }
        return if (TextUtils.isEmpty(msg)) getString(R.string.unknown_error) else msg!!
    }

    protected open fun getHttpExceptionMessage(httpException: HttpException): String {
        return getString(R.string.server_error)
    }

    protected open fun onTryAgain(tag: String) {}

    private fun makeFullscreen() {
        val decorView = window.decorView
        val uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE)
        decorView.systemUiVisibility = uiOptions
    }

    protected fun openCamera(fileName: String? = null) {
        mCapturedPath = DeviceUtil.openCamera(this, fileName)
    }

    protected fun openGallery() {
        DeviceUtil.openGallery(this)
    }

    private fun callToPhoneNumber(phoneNumber: String) {
        val telMgr = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val simState = telMgr.simState
        if (simState == TelephonyManager.SIM_STATE_ABSENT) {
            return
        }
        mCurrentPhoneNumber = phoneNumber
        DeviceUtil.callToPhoneNumber(this, mCurrentPhoneNumber!!)
    }

    protected open fun showFullScreen(): Boolean {
        return false
    }

    protected open fun onCapturedImage(path: String) {}

    protected open fun onChoseImage(uri: Uri?) {}

    protected open fun onChoseNoImage() {}

    protected open fun onRejectedCameraPermission() {}

    protected open fun onRejectedReadExternalPermission() {}

    protected open fun onRejectedPhoneCallPermission() {}

    protected open fun onAgreedWriteExternal() {}

    protected open fun onRejectedWriteExternalPermission() {}

    protected open fun onAgreedLocationPermission() {}

    protected open fun onRejectedLocationPermission() {}

    private fun showLoadingDialog(onLoadingDialogListener: LoadingDialog.OnLoadingDialogListener? = null) {
        if (mLoadingDialog == null)
            mLoadingDialog = LoadingDialog(this)

        if (mLoadingDialog!!.isShowing)
            return

        mLoadingDialog?.showWithListener(onLoadingDialogListener)
    }

    private fun hideLoadingDialog() {
        mLoadingDialog?.dismiss()
    }

    private fun getInformDialog(): InformDialog {
        if (mInformDialog == null)
            mInformDialog = InformDialog(this)
        return mInformDialog!!
    }

    fun getConfirmDialog(): ConfirmDialog {
        if (mConfirmDialog == null)
            mConfirmDialog = ConfirmDialog(this)
        return mConfirmDialog!!
    }
}