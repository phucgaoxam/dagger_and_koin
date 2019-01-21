package belp.myapplication.coordinator

import belp.base.injection.scope.ViewScope
import belp.data.common.AppComponent
import dagger.Component

@Component(modules = [CoordinatorModule::class], dependencies = [AppComponent::class])
@ViewScope
interface CoordinatorComponent {
    fun inject(activity: CoordinatorActivity)
}