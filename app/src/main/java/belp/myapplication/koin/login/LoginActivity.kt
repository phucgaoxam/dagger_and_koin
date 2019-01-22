package belp.myapplication.koin.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import belp.base.activity.BaseActivity
import belp.data.model.LoginRequest
import belp.data.model.LoginResponse
import belp.myapplication.R
import belp.myapplication.databinding.ActivityLoginKoinBinding
import belp.myapplication.koin.register.RegisterActivity
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class LoginActivity : BaseActivity<ActivityLoginKoinBinding, LoginViewModel>(),
    LoginView {

    override fun initViewModel() {
        mViewModel = getViewModel()
    }

    override fun onLoginSuccess(loginResponse: LoginResponse) {
    }

    override fun onRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private val temp: String by inject(name = "login")
    private val number: Int by inject(name = "loginNum") { parametersOf(100) }
    private val numberInPath: Int by inject(name = "loginPath.Integer") { parametersOf(69) }

    override fun getLayoutId(): Int {
        return R.layout.activity_login_koin
    }

    override fun getViewModelClass(): Class<LoginViewModel>? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewDataBinding.loginRequest = LoginRequest()
        mViewDataBinding.viewModel = mViewModel
        Log.e("LoginActivity", "temp: $temp\nnumber: $number\nnumberInPath: $numberInPath")
    }
}
