package belp.data.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("userNameOrEmailAddress") private var _userName: String = "",
    @SerializedName("password") private var _password: String = "",
    @SerializedName("rememberClient") private var _remember: Boolean = false,
    @SerializedName("tenantToken") var tenantToken: String = "",
    @SerializedName("device") var device: Device = Device()
) : BaseObservable() {
    var userName: String
        @Bindable get() = _userName
        set (value) {
            _userName = value
            notifyPropertyChanged(BR.userName)
        }
    var password: String
        @Bindable get() = _password
        set (value) {
            _password = value
            notifyPropertyChanged(BR.password)
        }
    var remember: Boolean
        @Bindable get() = _remember
        set (value) {
            _remember = value
            notifyPropertyChanged(BR.remember)
        }
}

data class LoginResponse(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("encryptedAccessToken") val encryptedAccessToken: String,
    @SerializedName("expireInSeconds") val expireInSeconds: Int,
    @SerializedName("userId") val userId: Int
)