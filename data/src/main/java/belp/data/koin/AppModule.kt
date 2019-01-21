package belp.data.koin

import android.content.Context
import belp.data.common.SharePreferenceManager
import belp.data.repository.UserRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val appModule = module {
    single { initSharedPreferencesManager(androidContext()) } bind SharePreferenceManager::class

    single { AppDomain(get() as UserRepository) } bind AppDomain::class
}

fun initSharedPreferencesManager(context: Context): SharePreferenceManager {
    return SharePreferenceManager(context)
}

val app = listOf(networkModule, appModule, imageLoaderModule, repositoryModule)