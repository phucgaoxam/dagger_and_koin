package belp.myapplication.main

import belp.base.fragment.BaseFragment
import belp.base.fragment.BaseFragmentHelper
import belp.base.fragment.FragmentProvider

class MainFragmentHelper(
    onChangedFragmentListener: OnChangedFragmentListener,
    fragmentProvider: FragmentProvider<BaseFragment<*, *>>,
    shouldShowPosition: Int
) : BaseFragmentHelper<BaseFragment<*, *>>(onChangedFragmentListener, fragmentProvider, shouldShowPosition)