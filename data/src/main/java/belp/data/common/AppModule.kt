package belp.data.common

import android.content.Context
import belp.base.injection.module.AppContextModule
import belp.base.injection.scope.ApplicationScope
import dagger.Module
import dagger.Provides

/**
 * Created by vophamtuananh on 3/13/18.
 */

@Module(includes = [(AppContextModule::class)])
class AppModule {

    @Provides
    @ApplicationScope
    fun sharePreferenceManager(context: Context): SharePreferenceManager {
        return SharePreferenceManager(context)
    }
}