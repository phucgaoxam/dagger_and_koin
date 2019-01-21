package belp.base.injection.module

import android.content.Context
import belp.base.injection.module.AppContextModule
import com.base.imageloader.ImageLoader
import belp.base.injection.scope.ApplicationScope
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by vophamtuananh on 1/13/18.
 */

@Module(includes = [AppContextModule::class])
class ImageLoaderModule {

    @Provides
    @ApplicationScope
    fun provideImageLoader(context: Context): ImageLoader {
        return ImageLoader(context)
    }
}