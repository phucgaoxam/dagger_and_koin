package belp.myapplication.facedetection

import belp.base.injection.scope.ViewScope
import belp.data.common.AppComponent
import dagger.Component

@Component(modules = [FaceDetectionModule::class], dependencies = [AppComponent::class])
@ViewScope
interface FaceDetectionComponent {
    fun inject(activity: FaceDetectionActivity)
}