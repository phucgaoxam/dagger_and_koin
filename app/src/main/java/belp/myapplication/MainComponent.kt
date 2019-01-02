package belp.myapplication

import belp.base.injection.scope.ViewScope
import belp.data.common.AppComponent
import dagger.Component

@Component(modules = [MainModule::class], dependencies = [AppComponent::class])
@ViewScope
interface MainComponent {
    fun inject(activity: MainActivity)
}