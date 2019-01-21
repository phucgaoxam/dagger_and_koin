package belp.myapplication.main.message

import belp.base.injection.scope.ViewScope
import belp.data.common.AppComponent
import dagger.Component

@Component(modules = [MessageModule::class], dependencies = [AppComponent::class])
@ViewScope
interface MessageComponent {
    fun inject(fragment: MessageFragment)
}