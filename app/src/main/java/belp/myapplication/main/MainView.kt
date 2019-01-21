package belp.myapplication.main

import belp.base.viewmodel.CommonView

interface MainView : CommonView {

    fun onQuery(query: String)
}