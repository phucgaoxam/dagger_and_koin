package belp.myapplication.main.contact

import belp.base.injection.scope.ViewScope
import belp.data.common.AppComponent
import dagger.Component

@Component(modules = [ContactModule::class], dependencies = [AppComponent::class])
@ViewScope
interface ContactComponent {
    fun inject(contactFragment: ContactFragment)
}