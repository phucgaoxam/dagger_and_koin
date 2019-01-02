package belp.myapplication.register

import belp.base.injection.scope.ViewScope
import belp.data.common.AppComponent
import dagger.Component

@Component(modules = [RegisterModule::class], dependencies = [AppComponent::class])
@ViewScope
interface RegisterComponent {
    fun inject(activity: RegisterActivity)
}