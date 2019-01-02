package belp.myapplication.splash

import android.content.Intent
import android.os.Bundle
import belp.base.activity.BaseActivity
import belp.myapplication.R
import belp.myapplication.databinding.ActivitySplashBinding
import belp.myapplication.login.LoginActivity
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {
    override fun getLayoutId(): Int = R.layout.activity_splash

    override fun getViewModelClass(): Class<SplashViewModel>? = SplashViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewDataBinding.viewModel = mViewModel
        waitForSplash()
    }

    private fun waitForSplash() {
        val action = { startApp() }
        val completable = Completable.fromAction(action)
        completable
            .delaySubscription(4, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread()).subscribe()
    }

    private fun startApp() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_TASK_ON_HOME

        startActivity(intent)
    }
}