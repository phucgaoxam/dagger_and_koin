package belp.base.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import belp.base.R
import belp.base.activity.BaseActivity
import belp.base.viewmodel.CommonView
import belp.base.viewmodel.FragmentViewModel

/**
 * Created by vophamtuananh on 1/7/18.
 */
abstract class BaseFragment<B : ViewDataBinding, VM : FragmentViewModel> : Fragment(), CommonView {

    protected lateinit var mViewDataBinding: B

    protected var mViewModel: VM? = null

    private var mIsInLeft: Boolean = false
    private var mIsOutLeft: Boolean = false
    private var mIsCurrentScreen: Boolean = false
    private var mIsPush: Boolean = false

    private var mInitialized = true
    protected var mViewCreated = false
    private var mViewDestroyed = false

    private var mWaitThread: WaitThread? = null

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun getViewModelClass(): Class<VM>?

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (getViewModelClass() != null)
            mViewModel = ViewModelProviders.of(this).get(getViewModelClass()!!)
        mViewModel?.onAttach(this)
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        val animation: Animation
        if (haveAnimation()) {
            if (mIsCurrentScreen) {
                val popExit = getPopExitAnimId()
                val pushEnter = getPushEnterAnimId()
                val pushExit = getPushExitAnimId()
                val popEnter = getPopEnterAnimId()
                animation = if (mIsPush)
                    AnimationUtils.loadAnimation(context, if (enter) pushEnter else pushExit)
                else
                    AnimationUtils.loadAnimation(context, if (enter) popEnter else popExit)
            } else {
                if (enter) {
                    val left = getLeftInAnimId()
                    val right = getRightInAnimId()
                    if (mIsInLeft) {
                        if (left == 0) {
                            animation = AlphaAnimation(1f, 1f)
                            animation.setDuration(resources.getInteger(R.integer.animation_time_full).toLong())
                        } else {
                            animation = AnimationUtils.loadAnimation(context, left)
                        }
                    } else {
                        if (right == 0) {
                            animation = AlphaAnimation(1f, 1f)
                            animation.setDuration(resources.getInteger(R.integer.animation_time_full).toLong())
                        } else {
                            animation = AnimationUtils.loadAnimation(context, right)
                        }
                    }
                } else {
                    val left = getLeftOutAnimId()
                    val right = getRightOutAnimId()
                    animation = AnimationUtils.loadAnimation(context, if (mIsOutLeft) left else right)
                }
            }
        } else {
            animation = AnimationUtils.loadAnimation(context, R.anim.no_anim)
        }

        if (enter) {
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    if (mViewDestroyed)
                        return
                    mWaitThread = WaitThread(this@BaseFragment)
                    mWaitThread?.start()
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
        }
        return animation
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return mViewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onVisible()
        mViewCreated = true
        mViewDestroyed = false
        mWaitThread?.continueProcessing()
    }

    override fun onDestroyView() {
        mWaitThread?.stopProcessing()
        mViewDestroyed = true
        mViewCreated = false
        onInVisible()
        mViewDataBinding.unbind()
        super.onDestroyView()
    }

    override fun showLoading() {
        val activity = activity
        if (activity != null && activity is BaseActivity<*, *>) {
            activity.showLoading()
        }
    }

    override fun hideLoading() {
        val activity = activity
        if (activity != null && activity is BaseActivity<*, *>) {
            activity.hideLoading()
        }
    }

    override fun showError(throwable: Throwable, tag: String) {
        val activity = activity
        if (activity != null && activity is BaseActivity<*, *>) {
            activity.showError(throwable, tag)
        }
    }

    override fun showError(throwable: Throwable) {
        val activity = activity
        if (activity != null && activity is BaseActivity<*, *>) {
            activity.showError(throwable)
        }
    }

    open fun onTryAgain(tag: String) {}

    fun getTagName(): String {
        return javaClass.simpleName
    }

    fun isInitialized(): Boolean {
        return mInitialized
    }

    open fun onInitialized() {
        mInitialized = false
        mViewModel?.onInitialized()
    }

    fun isViewCreated(): Boolean {
        return mViewCreated
    }

    fun setInLeft(inLeft: Boolean) {
        mIsInLeft = inLeft
    }

    fun setOutLeft(outLeft: Boolean) {
        mIsOutLeft = outLeft
    }

    fun setCurrentScreen(currentScreen: Boolean) {
        mIsCurrentScreen = currentScreen
    }

    fun setPush(push: Boolean) {
        mIsPush = push
    }

    open fun isShouldSave(): Boolean {
        return true
    }

    protected open fun onVisible() {}

    open fun onAfterInitialized() {
        mViewModel?.onAfterInitialized()
    }

    protected open fun onInVisible() {}

    open fun onCapturedImage(path: String) {}

    open fun onChoseImage(uri: Uri) {}

    protected open fun isHorizontal(): Boolean {
        return true
    }

    protected open fun haveAnimation(): Boolean {
        return true
    }

    protected open fun getPushExitAnimId(): Int {
        return if (!isHorizontal()) R.anim.slide_out_left else R.anim.push_exit
    }

    protected open fun getPopEnterAnimId(): Int {
        return if (!isHorizontal()) R.anim.slide_in_left else R.anim.pop_enter
    }

    protected open fun getPopExitAnimId(): Int {
        return if (!isHorizontal()) R.anim.slide_out_right else R.anim.pop_exit
    }

    protected open fun getPushEnterAnimId(): Int {
        return if (!isHorizontal()) R.anim.slide_in_right else R.anim.push_enter
    }

    protected open fun getLeftInAnimId(): Int {
        return if (isHorizontal()) R.anim.slide_in_left else R.anim.pop_enter
    }

    protected open fun getRightInAnimId(): Int {
        return if (isHorizontal()) R.anim.slide_in_right else R.anim.push_enter
    }

    protected open fun getLeftOutAnimId(): Int {
        return if (isHorizontal()) R.anim.slide_out_left else R.anim.push_exit
    }

    protected open fun getRightOutAnimId(): Int {
        return if (isHorizontal()) R.anim.slide_out_right else R.anim.pop_exit
    }
}