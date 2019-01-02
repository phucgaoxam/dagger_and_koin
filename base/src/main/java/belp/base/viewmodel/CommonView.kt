package belp.base.viewmodel

import androidx.lifecycle.LifecycleOwner

/**
 * Created by vophamtuananh on 1/7/18.
 */
interface CommonView : LifecycleOwner {

    fun showLoading()

    fun showError(throwable: Throwable, tag: String)

    fun showError(throwable: Throwable)

    fun hideLoading()
}