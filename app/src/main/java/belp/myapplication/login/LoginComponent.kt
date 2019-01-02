package belp.myapplication.login

import belp.base.injection.scope.ViewScope
import belp.data.common.AppComponent
import dagger.Component

@Component(modules = [LoginModule::class], dependencies = [AppComponent::class])
@ViewScope
interface LoginComponent {
    fun inject(activity: LoginActivity)
}