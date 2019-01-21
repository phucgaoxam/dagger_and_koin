package belp.myapplication.facedetection

import belp.base.viewmodel.ActivityViewModel

class FaceDetectionViewModel : ActivityViewModel() {
    fun onChooseImage() {
        val view : FaceDetectionView? = view()
        view?.onChooseImage()
    }
}