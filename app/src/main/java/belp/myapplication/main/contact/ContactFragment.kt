package belp.myapplication.main.contact

import android.os.Bundle
import belp.base.fragment.BaseInjectingFragment
import belp.myapplication.App
import belp.myapplication.R
import belp.myapplication.databinding.FragmentContactBinding

class ContactFragment : BaseInjectingFragment<FragmentContactBinding, ContactViewModel, ContactComponent>(),
    ContactView {

    companion object {
        @JvmStatic
        fun newInstance() = ContactFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }

    override fun createComponent(): ContactComponent? {
        return DaggerContactComponent.builder().appComponent(App.get(activity!!).component())
            .contactModule(ContactModule()).build()
    }

    override fun onInject(component: ContactComponent) {
        component.inject(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_contact
    }

    override fun getViewModelClass(): Class<ContactViewModel>? {
        return ContactViewModel::class.java
    }

}
