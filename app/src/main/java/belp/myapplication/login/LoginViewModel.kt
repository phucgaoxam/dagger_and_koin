package belp.myapplication.login

import android.util.Log
import android.view.View
import belp.base.viewmodel.ActivityViewModel
import belp.data.common.SharePreferenceManager
import belp.data.domain.AppDomain
import belp.data.model.Device
import belp.data.model.LoginRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by BelP on 09/12/2018.
 */
class LoginViewModel : ActivityViewModel() {

    private lateinit var mTenantToken: String
    private lateinit var mDevice: Device
    private lateinit var mAppDomain: AppDomain
    private lateinit var mSharedManager: SharePreferenceManager

    fun setAttributes(
        appDomain: AppDomain,
        tenantToken: String,
        device: Device,
        sharedPreferenceManager: SharePreferenceManager
    ) {
        mAppDomain = appDomain
        mTenantToken = tenantToken
        mDevice = device
        mSharedManager = sharedPreferenceManager
    }

    fun onRegister() {
        val view: LoginView? = view()
        view?.onRegister()
    }

    fun onLogin(loginRequest: LoginRequest) {
        val view: LoginView? = view()
        Log.e("login", "login")
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