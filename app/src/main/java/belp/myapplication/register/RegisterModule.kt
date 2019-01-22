package belp.myapplication.register

import belp.base.injection.scope.ViewScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class RegisterModule(private val activity: RegisterActivity) {

    @Provides
    @ViewScope
    @Named("className")
    fun getClassName(): String {
        return activity.javaClass.name
    }
}