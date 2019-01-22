package belp.myapplication.register

import android.util.Log
import belp.base.viewmodel.ActivityViewModel
import belp.data.domain.AppDomain
import belp.data.model.CreatedUser
import belp.data.model.RegisterRequest
import belp.myapplication.koin.register.RegisterView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RegisterViewModel : ActivityViewModel() {

    private lateinit var mAppDomain: AppDomain
    private lateinit var mTenantToken: String

    fun setAttributes(appDomain: AppDomain, tenantToken: String) {
        mAppDomain = appDomain
        mTenantToken = tenantToken
    }

    fun onRegister(createdUser: CreatedUser) {
        val view: RegisterView? = view()

        view?.let {
            val request = RegisterRequest(
                "http://www.wjacommunications.com/wp-content/uploads/2018/11/cat.jpg",
                createdUser.userName,
                createdUser.name,
                createdUser.name,
                createdUser.emailAddress,
                createdUser.password,
                mTenantToken
            )

            addDisposable(
                mAppDomain.register(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { view.showLoading() }
                    .doFinally { view.hideLoading() }
                    .subscribe(
                        { response -> view.onRegisterSuccess(response.createdUser) },
                        { error ->
                            Log.e("error register", "${error.message} - ${error.stackTrace}")
                            view.showError(error)
                        }
                    )
            )
        }
    }

    fun onChooseAvatar() {
        val view: RegisterView? = view()
        view?.chooseImage()
    }
}