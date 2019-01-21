package belp.myapplication.main.group

import belp.base.injection.scope.ViewScope
import belp.data.common.AppComponent
import dagger.Component

@Component(modules = [GroupModule::class], dependencies = [AppComponent::class])
@ViewScope
interface GroupComponent {
    fun inject(fragment: GroupFragment)
}