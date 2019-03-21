package belp.myapplication.splash

import belp.base.viewmodel.ActivityViewModel

class SplashViewModel: ActivityViewModel() {

    fun koin() {
        val view : SplashView? = view()
        view?.run {
            onKoin()
        }
    }

    fun dagger() {
        val view : SplashView? = view()
        view?.run {
            onDagger()
        }
    }
}