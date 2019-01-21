package belp.myapplication.main.message

import android.os.Bundle
import belp.base.fragment.BaseInjectingFragment
import belp.myapplication.App
import belp.myapplication.R
import belp.myapplication.databinding.FragmentMessageBinding

class MessageFragment : BaseInjectingFragment<FragmentMessageBinding, MessageViewModel, MessageComponent>(),
    MessageView {

    companion object {
        @JvmStatic
        fun newInstance(): MessageFragment =
            MessageFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun getViewModelClass(): Class<MessageViewModel>? {
        return MessageViewModel::class.java
    }

    override fun createComponent(): MessageComponent? {
        return DaggerMessageComponent.builder().appComponent(App.get(activity!!).component())
            .messageModule(MessageModule()).build()
    }

    override fun onInject(component: MessageComponent) {
        component.inject(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_message
    }

}