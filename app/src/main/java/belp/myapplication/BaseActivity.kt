package belp.myapplication

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import belp.base.viewmodel.ActivityViewModel
import belp.base.viewmodel.CommonView

abstract class BaseActivity<V : ViewDataBinding, VM : ActivityViewModel> : AppCompatActivity(), CommonView {
    protected lateinit var mViewDataBinding: V

    private lateinit var mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())

        if (getViewModelClass() != null) {
            mViewModel = ViewModelProviders.of(this).get(getViewModelClass()!!)
        }

        mViewModel.onAttached(this)
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getViewModelClass(): Class<VM>?

    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(throwable: Throwable, tag: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(throwable: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}