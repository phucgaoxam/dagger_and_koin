package belp.data.model

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("avatarUrl") val avatarUrl: String,
    @SerializedName("userName") val userName: String,
    @SerializedName("name") val name: String,
    @SerializedName("surname") val surName: String,
    @SerializedName("emailAddress") val emailAddress: String,
    @SerializedName("password") val password: String,
    @SerializedName("tenantToken") val tenantToken: String
)

data class RegisterResponse(
    @SerializedName("result") val createdUser: CreatedUser,
    @SerializedName("success") val success: Boolean,
    @SerializedName("unAuthorizedRequest") val unAuthorizedRequest: Boolean
)