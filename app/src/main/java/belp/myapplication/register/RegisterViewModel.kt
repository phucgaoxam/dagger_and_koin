package belp.myapplication.register

import android.util.Log
import androidx.databinding.ObservableField
import belp.base.viewmodel.ActivityViewModel
import belp.data.domain.AppDomain
import belp.data.model.RegisterRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RegisterViewModel : ActivityViewModel() {
    val userName = ObservableField("")
    val name = ObservableField("")
    val password = ObservableField("")
    val email = ObservableField("")

    private lateinit var mAppDomain: AppDomain
    private lateinit var mTenantToken: String

    fun setAttributes(appDomain: AppDomain, tenantToken: String) {
        mAppDomain = appDomain
        mTenantToken = tenantToken
    }

    fun onRegister() {
        val view: RegisterView? = view()

        view?.let {
            val request = RegisterRequest(
                "http://www.wjacommunications.com/wp-content/uploads/2018/11/cat.jpg",
                userName.get()!!,
                name.get()!!,
                name.get()!!,
                email.get()!!,
                password.get()!!,
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
                        })
            )
        }
    }

    fun onChooseAvatar() {
        val view: RegisterView? = view()
        view?.chooseImage()
    }
}