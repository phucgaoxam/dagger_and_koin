package belp.data.common

import belp.base.injection.scope.ApplicationScope
import belp.data.repository.UserRepository
import com.base.imageloader.ImageLoader
import belp.base.injection.module.ImageLoaderModule
import dagger.Component
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by vophamtuananh on 3/13/18.
 */
@Component(modules = [ImageLoaderModule::class, RepositoryModule::class, AppModule::class])
@ApplicationScope
interface AppComponent {

    fun imageLoader(): ImageLoader

    fun sharePreferenceManager(): SharePreferenceManager

    fun userRepository(): UserRepository

    @Named("tenantToken")
    fun getToken(): String
}
