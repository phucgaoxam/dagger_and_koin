package belp.myapplication

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import belp.base.viewmodel.ActivityViewModel

abstract class BaseInjectingActivity<V : ViewDataBinding, VM : ActivityViewModel, Component> : BaseActivity<V, VM>() {
    private var mComponent: Component? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mComponent = createComponent()
        mComponent?.let {
            onInject(it)
        }
    }

    protected abstract fun createComponent(): Component

    protected abstract fun onInject(component: Component)
}