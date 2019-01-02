package belp.data.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("userNameOrEmailAddress") val userName: String,
    @SerializedName("password") val password: String,
    @SerializedName("rememberClient") val remember: Boolean,
    @SerializedName("tenantToken") val tenantToken: String,
    @SerializedName("device") val device: Device
)

data class LoginResponse(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("encryptedAccessToken") val encryptedAccessToken: String,
    @SerializedName("expireInSeconds") val expireInSeconds: Int,
    @SerializedName("userId") val userId: Int
)