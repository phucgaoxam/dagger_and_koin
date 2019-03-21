package belp.myapplication.splash

import android.content.Intent
import android.os.Bundle
import belp.base.activity.BaseActivity
import belp.myapplication.R
import belp.myapplication.dagger.register.RegisterActivity
import belp.myapplication.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>(), SplashView {

    override fun getLayoutId(): Int = R.layout.activity_splash

    override fun getViewModelClass(): Class<SplashViewModel>? = SplashViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewDataBinding.viewModel = mViewModel
    }

    private fun start(activity: Class<BaseActivity<*, *>>) {
        val intent = Intent(this, activity)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_TASK_ON_HOME

        startActivity(intent)
    }

    override fun onKoin() {
        start(belp.myapplication.koin.register.RegisterActivity::class.java as Class<BaseActivity<*, *>>)
    }

    override fun onDagger() {
        start(RegisterActivity::class.java as Class<BaseActivity<*, *>>)
    }
}