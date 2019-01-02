package belp.data.model

import com.google.gson.annotations.SerializedName

data class Device(
    @SerializedName("deviceId") val deviceId: String,
    @SerializedName("registrationId") val registrationId: String,
    @SerializedName("pushKitRegistrationId") val pushKitRegistrationId: String,
    @SerializedName("deviceType") val deviceType: String = "Android"
)