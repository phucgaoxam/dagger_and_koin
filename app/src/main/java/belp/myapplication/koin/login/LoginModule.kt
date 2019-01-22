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
        "fTnWKT96Lww:APA91bFPqfr6hq3ASI1TQm2vdowNnN1b42zfEenMrtXdB-rS3ct9vAg4ynDTQVVp4PO-BTSu3iBsNV1mqieOUKoYsRQnN0Jsd-D--8QMtyKFhkMg2XnSNmL4KgpuU8ZnNCUOsKTo6Ywx",
        ""
    )
}

private fun number(value: Int): Int {
    return value
}

private fun createTempString(): String {
    return "temp"
}