package belp.myapplication

import belp.base.injection.scope.ViewScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class MainModule {

    @Provides
    @Named("hello")
    @ViewScope
    fun getString(): String = "hello world"

    @Provides
    @Named("hello2")
    @ViewScope
    fun getString2(): String = "hello world2"
}