package belp.data.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class CreatedUser(
    @SerializedName("userName") val userName: String,
    @SerializedName("name") val name: String,
    @SerializedName("surname") val surName: String,
    @SerializedName("emailAddress") val emailAddress: String,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("lastLoginTime") val lastLoginTime: Date,
    @SerializedName("creationTime") val creationTime: Date,
    @SerializedName("id") val id: Int
)