package belp.myapplication.main.group

import android.os.Bundle
import belp.base.fragment.BaseInjectingFragment
import belp.myapplication.App
import belp.myapplication.R
import belp.myapplication.databinding.FragmentGroupBinding

class GroupFragment : BaseInjectingFragment<FragmentGroupBinding, GroupViewModel, GroupComponent>(), GroupView {

    companion object {
        @JvmStatic
        fun newInstance(): GroupFragment =
            GroupFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    override fun createComponent(): GroupComponent? {
        return DaggerGroupComponent.builder().appComponent(App.get(activity!!).component()).groupModule(GroupModule())
            .build()
    }

    override fun onInject(component: GroupComponent) {
        component.inject(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_group
    }

    override fun getViewModelClass(): Class<GroupViewModel>? {
        return GroupViewModel::class.java
    }

}