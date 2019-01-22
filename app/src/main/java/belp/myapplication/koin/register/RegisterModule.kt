package belp.myapplication.koin.register

import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val registerModule = module {
    scope("register") { (value: Float) ->
        float(value)
        //double()
    }
    factory("num") { (value: Int) -> num(value) }
    factory("className") { (activity: RegisterActivity) -> getClassName(activity) }
    viewModel { RegisterViewModel(get(), get(name = "tenantToken")) }
}

private fun float(value: Float): Float {
    return value
}

private fun double(): Double {
    return 70.0
}

private fun num(value: Int): Int {
    return value
}

private fun getClassName(activity: RegisterActivity): String {
    return activity.javaClass.name
}