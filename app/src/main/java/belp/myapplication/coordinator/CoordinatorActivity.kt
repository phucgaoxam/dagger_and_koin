package belp.myapplication.coordinator

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import belp.base.activity.BaseInjectingActivity
import belp.myapplication.App
import belp.myapplication.R
import belp.myapplication.databinding.ActivityCoordinatorBinding

class CoordinatorActivity :
    BaseInjectingActivity<ActivityCoordinatorBinding, CoordinatorViewModel, CoordinatorComponent>(), CoordinatorView {
    override fun createComponent(): CoordinatorComponent? =
        DaggerCoordinatorComponent.builder().appComponent(App.get(this).component())
            .coordinatorModule(CoordinatorModule()).build()

    override fun onInject(component: CoordinatorComponent) = component.inject(this)

    override fun getLayoutId(): Int = R.layout.activity_coordinator

    override fun getViewModelClass(): Class<CoordinatorViewModel>? = CoordinatorViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)


        mViewDataBinding.viewModel = mViewModel
    }
}