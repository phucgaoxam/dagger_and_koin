package belp.myapplication.facedetection

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Pair
import belp.base.activity.BaseInjectingActivity
import belp.myapplication.*
import belp.myapplication.databinding.ActivityFaceDetectionBinding
import belp.myapplication.facedetection.util.CameraSource
import belp.myapplication.facedetection.util.FaceContourGraphic
import belp.myapplication.facedetection.util.FaceDetectionProcessor
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions

class FaceDetectionActivity :
    BaseInjectingActivity<ActivityFaceDetectionBinding, FaceDetectionViewModel, FaceDetectionComponent>(),
    FaceDetectionView {
    private var mImageMaxWidth: Int? = null
    // Max height (portrait mode)
    private var mImageMaxHeight: Int? = null

    override fun onChooseImage() {
        openGallery()
    }

    override fun onChoseImage(uri: Uri?) {
        super.onChoseImage(uri)
        var bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        if (bitmap != null) {
            // Get the dimensions of the View
            val targetedSize = getTargetedWidthHeight()

            val targetWidth = targetedSize.first
            val maxHeight = targetedSize.second

            // Determine how much to scale down the image
            val scaleFactor = Math.max(
                bitmap.width.toFloat() / targetWidth.toFloat(),
                bitmap.height.toFloat() / maxHeight.toFloat()
            )

            val resizedBitmap = Bitmap.createScaledBitmap(
                bitmap,
                (bitmap.width / scaleFactor).toInt(),
                (bitmap.height / scaleFactor).toInt(),
                true
            )

            mViewDataBinding.imAvatar.setImageBitmap(resizedBitmap)
            bitmap = resizedBitmap
        }

        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val options = FirebaseVisionFaceDetectorOptions.Builder()
            .setPerformanceMode(FirebaseVisionFaceDetectorOptions.FAST)
            .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS)
            .build()

        mViewDataBinding.btnChoose.isEnabled = false
        val detector = FirebaseVision.getInstance().getVisionFaceDetector(options)
        detector.detectInImage(image)
            .addOnSuccessListener { faces ->
                mViewDataBinding.btnChoose.isEnabled = true
                processFaceContourDetectionResult(faces)
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                mViewDataBinding.btnChoose.isEnabled = true
                e.printStackTrace()
            }
    }

    private fun getImageMaxWidth(): Int? {
        if (mImageMaxWidth == null) {
            // Calculate the max width in portrait mode. This is done lazily since we need to
            // wait for
            // a UI layout pass to get the right values. So delay it to first time image
            // rendering time.
            mImageMaxWidth = mViewDataBinding.imAvatar.width
        }

        return mImageMaxWidth
    }

    // Returns max image height, always for portrait mode. Caller needs to swap width / height for
    // landscape mode.
    private fun getImageMaxHeight(): Int? {
        if (mImageMaxHeight == null) {
            // Calculate the max width in portrait mode. This is done lazily since we need to
            // wait for
            // a UI layout pass to get the right values. So delay it to first time image
            // rendering time.
            mImageMaxHeight = mViewDataBinding.imAvatar.height
        }

        return mImageMaxHeight
    }

    private fun getTargetedWidthHeight(): Pair<Int, Int> {
        val targetWidth: Int
        val targetHeight: Int
        val maxWidthForPortraitMode = getImageMaxWidth()!!
        val maxHeightForPortraitMode = getImageMaxHeight()!!
        targetWidth = maxWidthForPortraitMode
        targetHeight = maxHeightForPortraitMode
        return Pair(targetWidth, targetHeight)
    }


    private fun processFaceContourDetectionResult(faces: List<FirebaseVisionFace>) {
        // Task completed successfully
        if (faces.isEmpty()) {
            //showToast("No face found")
            return
        }
        mViewDataBinding.overlay.clear()
        for (i in faces.indices) {
            val face = faces[i]
            val faceGraphic = FaceContourGraphic(mViewDataBinding.overlay)
            mViewDataBinding.overlay.add(faceGraphic)
            faceGraphic.updateFace(face)
        }
    }

    override fun createComponent(): FaceDetectionComponent? {
        return DaggerFaceDetectionComponent.builder().appComponent(App.get(this).component())
            .faceDetectionModule(FaceDetectionModule()).build()
    }

    override fun onInject(component: FaceDetectionComponent) {
        component.inject(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_face_detection
    }

    override fun getViewModelClass(): Class<FaceDetectionViewModel>? {
        return FaceDetectionViewModel::class.java
    }

    private var cameraSource: CameraSource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //openCamera()
        mViewDataBinding.viewModel = mViewModel
        cameraSource = CameraSource(this, mViewDataBinding.overlay2)
        cameraSource?.setMachineLearningFrameProcessor(FaceDetectionProcessor())

        mViewDataBinding.camera.start(cameraSource, mViewDataBinding.overlay2)
    }
}