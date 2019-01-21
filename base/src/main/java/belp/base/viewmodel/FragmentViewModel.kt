package belp.base.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

/**
 * Created by vophamtuananh on 1/7/18.
 */
open class FragmentViewModel : ViewModel(), LifecycleObserver {

    @Volatile
    var mViewWeakReference: WeakReference<CommonView>? = null

    private var compositeDisposables: CompositeDisposable? = null

    protected inline fun <reified V : CommonView> view(): V? {
        if (mViewWeakReference == null || mViewWeakReference?.get() == null)
            return null
        return V::class.java.cast(mViewWeakReference?.get())
    }

    open fun onAttach(view: CommonView) {
        mViewWeakReference = WeakReference(view)
        view.lifecycle.addObserver(this)
    }

    open fun onInitialized() {}

    open fun onAfterInitialized() {}

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreated() {
        if (compositeDisposables == null)
            compositeDisposables = CompositeDisposable()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onPause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy() {
        val view = mViewWeakReference?.get()
        view?.lifecycle?.removeObserver(this)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposables?.dispose()
    }

    protected fun addDisposable(disposable: Disposable) {
        compositeDisposables?.add(disposable)
    }

    protected fun getDisposableSize(): Int? {
        return compositeDisposables?.size()
    }
}