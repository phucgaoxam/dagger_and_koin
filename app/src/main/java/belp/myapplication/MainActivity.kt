package belp.myapplication

import android.os.Bundle
import belp.base.activity.BaseInjectingActivity
import belp.myapplication.databinding.MainActivityBinding
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by BelP on 09/12/2018.
 */
class MainActivity : BaseInjectingActivity<MainActivityBinding, MainViewModel, MainComponent>(), MainView {
    override fun getViewModelClass(): Class<MainViewModel>? {
        return MainViewModel::class.java
    }

    override fun createComponent(): MainComponent {
        return DaggerMainComponent.builder().appComponent(App.get(this).component()).mainModule(MainModule()).build()
    }

    override fun onInject(component: MainComponent) {
        component.inject(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.main_activity
    }

    @field:[Inject Named("hello")]
    internal lateinit var mText: String
    // or if you inject a primitive
    //@set:[Inject Named("logoIcon")] var logoIcon: Int = 0

    @field:[Inject Named("hello2")]
    internal lateinit var mText2: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewDataBinding.tvHello.text = mText
        mViewDataBinding.tvHello2.text = mText2
    }
}