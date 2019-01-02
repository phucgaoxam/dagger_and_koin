package belp.data.repository

import belp.data.model.LoginRequest
import belp.data.model.LoginResponse
import belp.data.model.RegisterRequest
import belp.data.model.RegisterResponse
import io.reactivex.Single

interface UserRepository {

    fun login(loginRequest: LoginRequest): Single<LoginResponse>

    fun register(registerRequest: RegisterRequest) : Single<RegisterResponse>
}