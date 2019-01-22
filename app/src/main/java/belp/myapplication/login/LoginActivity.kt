package belp.myapplication.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import belp.base.activity.BaseInjectingActivity
import belp.data.common.SharePreferenceManager
import belp.data.domain.AppDomain
import belp.data.model.Device
import belp.data.model.LoginRequest
import belp.data.model.LoginResponse
import belp.myapplication.App
import belp.myapplication.R
import belp.myapplication.databinding.ActivityLoginBinding
import belp.myapplication.register.RegisterActivity
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by BelP on 09/12/2018.
 */
class LoginActivity : BaseInjectingActivity<ActivityLoginBinding, LoginViewModel, LoginComponent>(), LoginView {

    override fun onRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    override fun onLoginSuccess(loginResponse: LoginResponse) {
        Log.e("login", "login success: ${loginResponse.accessToken}")
    }

    override fun getViewModelClass(): Class<LoginViewModel>? {
        return LoginViewModel::class.java
    }

    override fun createComponent(): LoginComponent {
        return DaggerLoginComponent.builder()
            .appComponent(App.get(this).component())
            .loginModule(LoginModule(this))
            .build()
    }

    override fun onInject(component: LoginComponent) {
        component.inject(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewDataBinding.loginRequest = LoginRequest()
        mViewDataBinding.viewModel = mViewModel
    }

    @Inject
    fun setViewModelAttributes(
        appDomain: AppDomain,
        @Named("tenantToken") tenantToken: String,
        device: Device,
        sharedPreferenceManager: SharePreferenceManager
    ) {
        mViewModel?.setAttributes(appDomain, tenantToken, device, sharedPreferenceManager)
    }
}