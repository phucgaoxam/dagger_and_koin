package belp.data.common

import belp.base.injection.scope.ApplicationScope
import belp.data.repository.UserRepository
import belp.data.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Created by vophamtuananh on 3/13/18.
 */

@Module(includes = [NetworkModule::class])
class RepositoryModule {

    @Provides
    @ApplicationScope
    fun serverServices(retrofit: Retrofit): ServerServices {
        return retrofit.create(ServerServices::class.java)
    }

    @Provides
    @ApplicationScope
    fun userRepository(serverServices: ServerServices): UserRepository {
        return UserRepositoryImpl(serverServices)
    }
}