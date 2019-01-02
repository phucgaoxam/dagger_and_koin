package belp.data.common

import belp.data.model.LoginRequest
import belp.data.model.LoginResponse
import belp.data.model.RegisterRequest
import belp.data.model.RegisterResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by vophamtuananh on 3/13/18.
 */
interface ServerServices {

    companion object {
        const val IMAGE_TYPE = "image/*"
    }

    @POST("/api/ChatHubTokenAuth/Authenticate")
    fun login(@Body loginRequest: LoginRequest): Single<LoginResponse>

    @POST("/api/services/app/Client/Register")
    fun register(@Body registerRequest: RegisterRequest): Single<RegisterResponse>
}