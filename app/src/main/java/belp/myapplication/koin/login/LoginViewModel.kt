package belp.myapplication.koin.login

import android.view.View
import belp.base.viewmodel.ActivityViewModel
import belp.data.common.SharePreferenceManager
import belp.data.koin.AppDomain
import belp.data.model.Device
import belp.data.model.LoginRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by BelP on 09/12/2018.
 */
class LoginViewModel(
    private val mAppDomain: AppDomain,
    private val mTenantToken: String,
    private val mDevice: Device,
    private val mSharedManager: SharePreferenceManager
) : ActivityViewModel() {

    fun onRegister() {
        val view: LoginView? = view()
        view?.onRegister()
    }

    fun onLogin(loginRequest: LoginRequest) {
        val view: LoginView? = view()
        loginRequest.tenantToken = mTenantToken
        loginRequest.device = mDevice

        view?.let {
            addDisposable(
                mAppDomain.login(loginRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { view.showLoading() }
                    .doFinally { view.hideLoading() }
                    .subscribe(
                        { result ->
                            view.onLoginSuccess(result)
                            mSharedManager.accessToken = result.accessToken
                        },
                        { error -> view.showError(error) }
                    )
            )
        }
    }

    fun onRemember(view: View, value: Boolean) {

    }
}