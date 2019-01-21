package belp.myapplication.koin

import android.os.Bundle
import android.util.Log
import belp.base.activity.BaseActivity
import belp.data.model.LoginRequest
import belp.data.model.LoginResponse
import belp.myapplication.R
import belp.myapplication.databinding.ActivityLoginKoinBinding
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<ActivityLoginKoinBinding, LoginViewModel>(), LoginView {
    override fun initViewModel() {
        mViewModel = viewModel<LoginViewModel>().value
    }

    override fun onLoginSuccess(loginResponse: LoginResponse) {
    }

    override fun onRegister() {
    }

    private val temp: String by inject(name = "login")
    private val number: Int by inject()

    override fun getLayoutId(): Int {
        return R.layout.activity_login_koin
    }

    override fun getViewModelClass(): Class<LoginViewModel>? {
        return LoginViewModel::class.java
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewDataBinding.loginRequest = LoginRequest()
        mViewDataBinding.viewModel = mViewModel
        Log.e("temp", "$temp $number")
    }
}
