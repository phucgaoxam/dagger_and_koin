package belp.data.koin

import android.content.Context
import com.base.imageloader.ImageLoader
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val imageLoaderModule = module {
    single { getImageLoader(androidContext()) }
}

private fun getImageLoader(context: Context): ImageLoader {
    return ImageLoader(context)
}