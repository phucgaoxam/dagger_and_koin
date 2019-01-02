package belp.data.common

import belp.base.injection.scope.ApplicationScope
import com.base.imageloader.ImageLoader
import com.base.injection.module.ImageLoaderModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by vophamtuananh on 3/13/18.
 */
@Singleton
@Component(modules = [ImageLoaderModule::class, RepositoryModule::class, AppModule::class])
@ApplicationScope
interface AppComponent {

    fun imageLoader(): ImageLoader

    fun sharePreferenceManager(): SharePreferenceManager
}
