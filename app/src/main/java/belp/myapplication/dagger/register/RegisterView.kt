package belp.myapplication.dagger.register

import belp.base.viewmodel.CommonView
import belp.data.model.CreatedUser

interface RegisterView : CommonView {
    fun onRegisterSuccess(result: CreatedUser)

    fun chooseImage()
}