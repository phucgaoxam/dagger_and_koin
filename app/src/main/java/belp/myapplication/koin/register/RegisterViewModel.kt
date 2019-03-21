package belp.myapplication.koin.register

import android.util.Log
import belp.base.viewmodel.ActivityViewModel
import belp.data.koin.AppDomain
import belp.data.model.CreatedUser
import belp.data.model.RegisterRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RegisterViewModel(
    private val mAppDomain: AppDomain,
    private val mTenantToken: String
) : ActivityViewModel() {

    override fun onDestroy() {
        super.onDestroy()
        Log.e("destroy", "destroy view model")
    }

    fun onRegister(createdUser: CreatedUser) {
        val view: RegisterView? = view()

        view?.run {
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
                    .doOnSubscribe { showLoading() }
                    .doFinally { hideLoading() }
                    .subscribe(
                        { response -> onRegisterSuccess(response.createdUser) },
                        //  { error -> view.showError(error) }
                        { onRegisterSuccess(createdUser) }
                    )
            )
        }
    }

    fun onChooseAvatar() {
        val view: RegisterView? = view()
        view?.chooseImage()
    }
}