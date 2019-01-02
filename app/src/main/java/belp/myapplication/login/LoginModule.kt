package belp.myapplication.login

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import belp.base.injection.scope.ViewScope
import belp.data.model.Device
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class LoginModule(private val activity: LoginActivity) {

    @Provides
    @ViewScope
    fun context(): Context {
        return activity
    }

    @SuppressLint("HardwareIds")
    @Provides
    @ViewScope
    fun getDevice(context: Context): Device {
        val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

        return Device(
            deviceId,
            "fTnWKT96Lww:APA91bFPqfr6hq3ASI1TQm2vdowNnN1b42zfEenMrtXdB-rS3ct9vAg4ynDTQVVp4PO-BTSu3iBsNV1mqieOUKoYsRQnN0Jsd-D--8QMtyKFhkMg2XnSNmL4KgpuU8ZnNCUOsKTo6Ywx",
            ""
        )
    }
}