package belp.myapplication

import android.app.Activity
import androidx.multidex.MultiDexApplication
import belp.base.injection.module.AppContextModule
import belp.base.injection.module.ImageLoaderModule
import belp.data.common.AppComponent
import belp.data.common.AppModule
import belp.data.common.DaggerAppComponent
import belp.data.koin.app
import belp.myapplication.koin.login.loginModule
import belp.myapplication.koin.register.registerModule
import org.koin.android.ext.android.startKoin
import org.koin.android.logger.AndroidLogger

/**
 * Created by BelP on 07/12/2018.
 */
class App : MultiDexApplication() {

    private lateinit var appComponent: AppComponent

    companion object {
        fun get(activity: Activity): App {
            return App::class.java.cast(activity.application)!!
        }
    }

    override fun onCreate() {
        super.onCreate()

        ///Start Koin
        startKoin(this, app + loginModule + registerModule, logger = AndroidLogger(showDebug = true))

        ///Init AppComponent
        appComponent = DaggerAppComponent.builder().appModule(AppModule())
            .imageLoaderModule(ImageLoaderModule())
            .appContextModule(AppContextModule(applicationContext)).build()
    }

    fun component(): AppComponent = appComponent
}