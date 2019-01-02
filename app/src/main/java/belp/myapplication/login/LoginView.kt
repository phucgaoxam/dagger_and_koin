package belp.myapplication.login

import belp.base.viewmodel.CommonView
import belp.data.model.LoginResponse

/**
 * Created by BelP on 09/12/2018.
 */
interface LoginView : CommonView {
    fun onLoginSuccess(loginResponse: LoginResponse)

    fun onRegister()
}