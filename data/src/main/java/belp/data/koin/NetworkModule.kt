package belp.data.koin

import android.content.Context
import belp.base.exception.NoConnectionException
import belp.data.R
import belp.data.common.SharePreferenceManager
import com.base.utils.NetworkUtil
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Modifier
import java.util.concurrent.TimeUnit

private const val TIME_OUT = 10
private const val KEY_CONTENT_TYPE = "Content-Type"
private const val KEY_AUTHORIZATION = "Authorization"
private const val VALUE_CONTENT_TYPE = "application/json"
private const val PRO_DOMAIN = "http://chathub.saigontechnology.vn/"
private const val TENANT_TOKEN = "ef0df4b9841a43a2ac8f2fd0b63c7bd7"
private const val SIGNAL_R_DOMAIN = "http://chathub.saigontechnology.vn/signalr-myChatHub"

val networkModule = module {
    single { createHttpLoggingInterceptor(debug()) }

    single { okHttpClient(get(), get(), get()) }

    single { createGson() }

    single("baseUrl") { baseUrl(debug()) }

    single("tenantToken") { getTenantToken() }

    single { createRetrofit(get(name = "baseUrl"), get(), get()) }
}

private fun createHttpLoggingInterceptor(debug: Boolean): HttpLoggingInterceptor {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = if (debug) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    return interceptor
}

private fun createRetrofit(baseUrl: String, okHttpClient: OkHttpClient, gson: Gson): Retrofit {
    val builder = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

    return builder.build()
}


private fun debug(): Boolean {
    return true
}


private fun okHttpClient(
    context: Context,
    sharedPreferenceManager: SharePreferenceManager,
    interceptor: HttpLoggingInterceptor
): OkHttpClient {
    val okBuilder = OkHttpClient.Builder()
    okBuilder.addInterceptor(interceptor)
    okBuilder.addInterceptor { chain ->
        if (!NetworkUtil.isConnected(context))
            throw  NoConnectionException(context.resources.getString(R.string.no_network))
        val request = chain.request()
        val builder = request.newBuilder()

        val headers = HashMap<String, String>()
        headers[KEY_CONTENT_TYPE] = VALUE_CONTENT_TYPE
        headers[KEY_AUTHORIZATION] = "Bearer ${sharedPreferenceManager.accessToken}"

        for ((key: String, value: String) in headers) {
            builder.addHeader(key, value)
        }
        chain.proceed(builder.build())
    }

    okBuilder.connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
    okBuilder.readTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
    okBuilder.writeTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)

    return okBuilder.build()
}

private fun createGson(): Gson {
    val gsonBuilder = GsonBuilder().excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT)
    return gsonBuilder.create()
}

private fun getTenantToken(): String {
    return TENANT_TOKEN
}

private fun baseUrl(debug: Boolean): String {
    return PRO_DOMAIN
}