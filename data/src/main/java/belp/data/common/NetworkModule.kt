package belp.data.common

import android.content.Context
import android.util.Log
import belp.base.injection.module.AppContextModule
import belp.data.R
import belp.base.exception.NoConnectionException
import belp.base.injection.scope.ApplicationScope
import com.base.utils.NetworkUtil
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Modifier
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Named

/**
 * Created by vophamtuananh on 3/13/18.
 */

@Module(includes = [(AppContextModule::class), (AppModule::class)])
class NetworkModule {
    companion object {
        private const val TIME_OUT = 10
        private const val KEY_CONTENT_TYPE = "Content-Type"
        private const val KEY_AUTHORIZATION = "Authorization"
        private const val VALUE_CONTENT_TYPE = "application/json"
        private const val PRO_DOMAIN = "http://fakehttp.vn/"
        private const val TENANT_TOKEN = "ef0df4b9841a43a2ac8f2fd0b63c7bd7"
    }

    @Provides
    @ApplicationScope
    fun retrofit(@Named("baseUrl") baseUrl: String, okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        val builder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        return builder.build()
    }

    @Provides
    @ApplicationScope
    fun httpLoggingInterceptor(debug: Boolean): HttpLoggingInterceptor {
        Log.e("init", "init httpInterceptor")
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (debug) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return interceptor
    }

    @Provides
    @ApplicationScope
    fun okHttpClient(
        context: Context,
        sharePreferenceManager: SharePreferenceManager,
        interceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        val okBuilder = OkHttpClient.Builder()
        okBuilder.addInterceptor(interceptor)
        okBuilder.addInterceptor { chain ->
            if (!NetworkUtil.isConnected(context))
                throw NoConnectionException(context.getString(R.string.no_network))
            val request = chain.request()
            val builder = request.newBuilder()

            val headers = HashMap<String, String>()
            headers[KEY_CONTENT_TYPE] = VALUE_CONTENT_TYPE
            headers[KEY_AUTHORIZATION] = "Bearer " + sharePreferenceManager.accessToken
            for ((key, value) in headers) {
                builder.addHeader(key, value)
            }
            chain.proceed(builder.build())
        }

        okBuilder.connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
        okBuilder.readTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
        okBuilder.writeTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)

        return okBuilder.build()
    }

    @Provides
    @ApplicationScope
    fun gson(): Gson {
        val gsonBuilder = GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.STATIC)
        return gsonBuilder.create()
    }

    @Provides
    @Named("baseUrl")
    fun baseUrl(debug: Boolean): String {
        return PRO_DOMAIN
    }

    @Provides
    fun debug(): Boolean {
        return true
    }

    @Provides
    @Named("tenantToken")
    fun getTenantToken(): String {
        return TENANT_TOKEN
    }
}