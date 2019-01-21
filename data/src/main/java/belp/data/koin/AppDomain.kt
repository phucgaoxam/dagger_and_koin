package belp.data.koin

import belp.data.model.LoginRequest
import belp.data.model.LoginResponse
import belp.data.model.RegisterRequest
import belp.data.model.RegisterResponse
import belp.data.repository.UserRepository
import io.reactivex.Single

class AppDomain(private val mUserRepository: UserRepository) {
    fun login(loginRequest: LoginRequest): Single<LoginResponse> {
        return mUserRepository.login(loginRequest)
    }

    fun register(registerRequest: RegisterRequest): Single<RegisterResponse> {
        return mUserRepository.register(registerRequest)
    }

    fun clean() {

    }
}