package belp.data.repository

import belp.data.common.ServerServices
import belp.data.model.LoginRequest
import belp.data.model.LoginResponse
import belp.data.model.RegisterRequest
import belp.data.model.RegisterResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class UserRepositoryImpl(private val mServerServices: ServerServices) : UserRepository {
    override fun register(registerRequest: RegisterRequest): Single<RegisterResponse> {
        return mServerServices.register(registerRequest)
            .subscribeOn(Schedulers.io())
    }

    override fun login(loginRequest: LoginRequest): Single<LoginResponse> {
        return mServerServices.login(loginRequest)
            .subscribeOn(Schedulers.io())
    }
}