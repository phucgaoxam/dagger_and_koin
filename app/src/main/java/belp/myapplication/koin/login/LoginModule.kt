package belp.myapplication.koin.login

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import belp.data.model.Device
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val loginModule = module {
    module("loginPath") {
        single { (value: Int) -> number(value) }
    }
    single("loginNum") { (value: Int) -> number(value) }
    single("login") { createTempString() }
    viewModel { LoginViewModel(get(), get(name = "tenantToken"), get(), get()) }
    single { getDevice(androidContext()) } bind Device::class
}

@SuppressLint("HardwareIds")
private fun getDevice(context: Context): Device {
    val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    return Device(
        deviceId,
        "",
        ""
    )
}

private fun number(value: Int): Int {
    return value
}

private fun createTempString(): String {
    return "temp"
}