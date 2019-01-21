package belp.data.koin

import belp.data.common.ServerServices
import belp.data.repository.UserRepository
import belp.data.repository.UserRepositoryImpl
import org.koin.dsl.module.module
import retrofit2.Retrofit

val repositoryModule = module {
    single { serverServices(get() as Retrofit) } bind ServerServices::class

    single { userRepository(get() as ServerServices) } bind UserRepository::class
}

private fun serverServices(retrofit: Retrofit): ServerServices {
    return retrofit.create(ServerServices::class.java)
}

private fun userRepository(serverServices: ServerServices): UserRepository {
    return UserRepositoryImpl(serverServices)
}