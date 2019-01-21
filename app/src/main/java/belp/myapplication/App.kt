package belp.myapplication

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.multidex.MultiDexApplication
import belp.base.injection.module.AppContextModule
import belp.data.common.AppComponent
import belp.data.common.AppModule
import belp.data.common.DaggerAppComponent
import belp.base.injection.module.ImageLoaderModule
import belp.data.koin.app
import belp.data.koin.appModule
import belp.myapplication.koin.loginModule
import org.koin.android.ext.android.get
import org.koin.android.ext.android.startKoin

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
        startKoin(this, app + loginModule)
        val s = get<String>(name = "tenantToken")
        Log.e("baseUrl", s)

        appComponent = DaggerAppComponent.builder().appModule(AppModule())
            .imageLoaderModule(ImageLoaderModule())
            .appContextModule(AppContextModule(applicationContext)).build()
    }

    fun component(): AppComponent = appComponent
}