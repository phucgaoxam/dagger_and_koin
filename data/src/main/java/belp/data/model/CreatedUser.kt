package belp.data.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.google.gson.annotations.SerializedName
import java.util.*

data class CreatedUser(
    @SerializedName("userName") var _userName: String = "",
    @SerializedName("name") var _name: String = "",
    @SerializedName("surname") var _surName: String = "",
    @SerializedName("emailAddress") var _emailAddress: String = "",
    var _password: String = "",
    @SerializedName("isActive") val isActive: Boolean = false,
    @SerializedName("fullName") val fullName: String = "",
    @SerializedName("lastLoginTime") val lastLoginTime: Date = Calendar.getInstance().time,
    @SerializedName("creationTime") val creationTime: Date = Calendar.getInstance().time,
    @SerializedName("id") val id: Int = 0
) : BaseObservable() {
    var userName: String
        @Bindable get() = _userName
        set(value) {
            _userName = value
            notifyPropertyChanged(BR.userName)
        }
    var name: String
        @Bindable get() = _name
        set(value) {
            _name = value
            notifyPropertyChanged(BR.name)
        }
    var emailAddress: String
        @Bindable get() = _emailAddress
        set (value) {
            _emailAddress = value
            notifyPropertyChanged(BR.emailAddress)
        }
    var password: String
        @Bindable get() = _password
        set (value) {
            _password = value
            notifyPropertyChanged(BR.password)
        }
}