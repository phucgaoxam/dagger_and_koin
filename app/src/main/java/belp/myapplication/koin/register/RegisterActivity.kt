package belp.myapplication.koin.register

import android.net.Uri
import android.os.Bundle
import android.util.Log
import belp.base.activity.BaseActivity
import belp.data.model.CreatedUser
import belp.myapplication.R
import belp.myapplication.databinding.ActivityRegisterKoinBinding
import com.bumptech.glide.Glide
import org.koin.android.ext.android.inject
import org.koin.android.scope.ext.android.bindScope
import org.koin.android.scope.ext.android.getOrCreateScope
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope

class RegisterActivity : BaseActivity<ActivityRegisterKoinBinding, RegisterViewModel>(),
    RegisterView {

    private val mClassName: String by inject(name = "className") { parametersOf(this) }

    private val mNum: Int by inject(name = "num") { parametersOf(3) }

    private lateinit var mScope: Scope

    private val mFloat: Float by inject { parametersOf(69f) }

    override fun getViewModelClass(): Class<RegisterViewModel>? = null

    override fun chooseImage() {
        openGallery()
    }

    override fun initViewModel() {
        mViewModel = viewModel<RegisterViewModel>().value
    }

    override fun getLayoutId(): Int = R.layout.activity_register_koin

    override fun onRegisterSuccess(result: CreatedUser) {
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mScope = getOrCreateScope("register")
        bindScope(mScope)
        super.onCreate(savedInstanceState)
        mViewDataBinding.model = CreatedUser()
        mViewDataBinding.viewModel = mViewModel

        Log.e("RegisterActivity", "className: $mClassName\nNumber: $mNum\nFloat: $mFloat")
    }

    override fun onChoseImage(uri: Uri?) {
        super.onChoseImage(uri)

        Glide.with(this).load(uri).into(mViewDataBinding.imAvatar)
    }

    override fun onDestroy() {
        mScope.close()
        super.onDestroy()
    }
}