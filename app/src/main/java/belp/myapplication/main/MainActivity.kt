package belp.myapplication.main

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.Button
import androidx.viewpager.widget.ViewPager
import belp.base.activity.BaseInjectingActivity
import belp.myapplication.App
import belp.myapplication.R
import belp.myapplication.databinding.ActivityMainBinding
import belp.myapplication.generated.callback.OnClickListener
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFaceContour
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.disposables.Disposable
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

typealias CustomFunction = (Button, OnClickListener) -> Unit

class MainActivity : BaseInjectingActivity<ActivityMainBinding, MainViewModel, MainComponent>(), MainView {

    override fun onQuery(query: String) {
    }

    @Inject
    lateinit var mFragmentPagerAdapter: MainPagerAdapter

    private val mQuery = HashMap<String, String>()
    private lateinit var mSubscription: Disposable
    private var mPage = 0

    override fun createComponent(): MainComponent? {
        return DaggerMainComponent.builder().appComponent(App.get(this).component()).mainModule(MainModule(this))
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewDataBinding.viewModel = mViewModel
        mViewDataBinding.vpMain.adapter = mFragmentPagerAdapter

        mViewDataBinding.bnvMain.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_message -> {
                    mViewDataBinding.vpMain.currentItem = 0
                }
                R.id.action_contact -> mViewDataBinding.vpMain.currentItem = 1
                else -> mViewDataBinding.vpMain.currentItem = 2
            }

            it.isChecked = true
            false
        }

        val textObservable = mViewDataBinding.etSearch.textChanges().doOnSubscribe {
            mViewDataBinding.imClear.visibility =
                    if (mViewDataBinding.etSearch.text!!.isEmpty()) View.GONE else View.VISIBLE
        }.map { mQuery[mPage.toString()] = it.toString() }
            .debounce(1000L, TimeUnit.MILLISECONDS)

        mSubscription = textObservable.subscribe {
            Log.e("tag", it.toString())

        }

        mViewDataBinding.imAvatar.setOnClickListener {
            openGallery()
        }

        mViewDataBinding.vpMain.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                mPage = position
                mViewDataBinding.etSearch.setText(mQuery[position.toString()])

                if (mViewDataBinding.etSearch.text!!.isNotEmpty())
                    mViewDataBinding.etSearch.setSelection(mViewDataBinding.etSearch.length())

                when (position) {
                    0 -> mViewDataBinding.etSearch.hint = "Search chat..."
                    1 -> mViewDataBinding.etSearch.hint = "Search contact..."
                    else -> mViewDataBinding.etSearch.hint = "Search groups..."
                }
            }
        })
    }

    override fun onChoseImage(uri: Uri?) {
        super.onChoseImage(uri)
        val image = FirebaseVisionImage.fromFilePath(this, uri!!)

        val options =
            FirebaseVisionFaceDetectorOptions.Builder().setContourMode(FirebaseVisionFaceContour.ALL_POINTS).build()

        val detector = FirebaseVision.getInstance().getVisionFaceDetector(options)

        val result = detector.detectInImage(image).addOnSuccessListener { faces ->
            Log.e("success", faces.size.toString())
            mViewDataBinding.imAvatar.setImageURI(uri)

            faces.forEach {
                val contour = it.getContour(FirebaseVisionFaceContour.FACE)
                contour.points.forEach { point ->
                    Log.e("point", "point: $point")
                }
            }
        }.addOnFailureListener { exception -> Log.e("failed get face", exception.toString()) }
    }

    override fun onDestroy() {
        mSubscription.dispose()
        super.onDestroy()
    }

    override fun onInject(component: MainComponent) {
        component.inject(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getViewModelClass(): Class<MainViewModel>? {
        return MainViewModel::class.java
    }
}

class DrawView : View {
    private var mRect: Rect? = null
    private val mPaint = Paint()

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    )

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        mPaint.strokeWidth = 3.0f
        if (mRect != null) {
            canvas?.drawRect(mRect!!, mPaint)
        }
    }

    fun setRect(rect: Rect) {
        mRect = rect
        invalidate()
    }
}