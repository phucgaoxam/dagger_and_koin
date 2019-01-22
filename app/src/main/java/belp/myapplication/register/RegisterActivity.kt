package belp.myapplication.register

import android.net.Uri
import android.os.Bundle
import android.util.Log
import belp.base.activity.BaseInjectingActivity
import belp.data.domain.AppDomain
import belp.data.model.CreatedUser
import belp.myapplication.App
import belp.myapplication.R
import belp.myapplication.databinding.ActivityRegisterBinding
import com.bumptech.glide.Glide
import javax.inject.Inject
import javax.inject.Named

class RegisterActivity : BaseInjectingActivity<ActivityRegisterBinding, RegisterViewModel, RegisterComponent>(),
    RegisterView {

    @field:[Inject Named("className")]
    lateinit var mClassName: String

    override fun getViewModelClass(): Class<RegisterViewModel>? = RegisterViewModel::class.java

    override fun chooseImage() {
        openGallery()
    }

    override fun createComponent(): RegisterComponent? {
        return DaggerRegisterComponent.builder()
            .appComponent(App.get(this).component())
            .registerModule(RegisterModule(this))
            .build()
    }

    override fun onInject(component: RegisterComponent) {
        component.inject(this)
    }

    override fun getLayoutId(): Int = R.layout.activity_register

    override fun onRegisterSuccess(result: CreatedUser) {
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewDataBinding.model = CreatedUser()
        mViewDataBinding.viewModel = mViewModel

        Log.e("className", mClassName)
    }

    @Inject
    fun setViewModelAttributes(appDomain: AppDomain, @Named("tenantToken") tenantToken: String) {
        mViewModel?.setAttributes(appDomain, tenantToken)
    }

    override fun onChoseImage(uri: Uri?) {
        super.onChoseImage(uri)

        Glide.with(this).load(uri).into(mViewDataBinding.imAvatar)
    }
}