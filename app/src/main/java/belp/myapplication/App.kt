package belp.myapplication

import android.app.Activity
import android.app.Application
import belp.base.injection.module.AppContextModule
import belp.data.common.AppComponent
import belp.data.common.AppModule
import belp.data.common.DaggerAppComponent
import com.base.injection.module.ImageLoaderModule

/**
 * Created by BelP on 07/12/2018.
 */
class App : Application() {

    private lateinit var appComponent: AppComponent

    companion object {
        fun get(activity: Activity): App {
            return App::class.java.cast(activity.application)!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule())
            .imageLoaderModule(ImageLoaderModule())
            .appContextModule(AppContextModule(applicationContext)).build()
    }

    fun component(): AppComponent = appComponent
}