package belp.myapplication.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import belp.myapplication.main.contact.ContactFragment
import belp.myapplication.main.group.GroupFragment
import belp.myapplication.main.message.MessageFragment
import javax.inject.Inject

class MainPagerAdapter @Inject constructor(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MessageFragment.newInstance()
            1 -> ContactFragment.newInstance()
            else -> GroupFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return 3
    }
}