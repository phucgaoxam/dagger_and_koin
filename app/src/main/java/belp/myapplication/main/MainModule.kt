package belp.myapplication.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import belp.myapplication.main.contact.ContactFragment
import belp.myapplication.main.group.GroupFragment
import belp.myapplication.main.message.MessageFragment
import dagger.Module
import dagger.Provides

@Module
class MainModule(private val activity: MainActivity) {

    @Provides
    fun fragmentManager(): FragmentManager {
        return activity.supportFragmentManager
    }

    @Provides
    fun fragments(): ArrayList<Fragment> {
        return arrayListOf(MessageFragment.newInstance(), ContactFragment.newInstance(), GroupFragment.newInstance())
    }
}