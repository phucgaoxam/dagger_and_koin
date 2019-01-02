package belp.base.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * Created by vophamtuananh on 12/24/17.
 */

inline fun <reified T> Activity.start(clearBackStack: Boolean = false, bundle: Bundle? = null) {
    val intent = Intent(this, T::class.java)
    if (clearBackStack)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    bundle?.let {
        intent.putExtras(bundle)
    }
    startActivity(intent)
}

inline fun <reified T> Activity.startForResult(requestCode: Int, bundle: Bundle? = null) {
    val intent = Intent(this, T::class.java)
    bundle?.let {
        intent.putExtras(bundle)
    }
    startActivityForResult(intent, requestCode)
}

/*inline fun <reified T : Fragment> createNewFragment(context: Context, bundle: Bundle? = null): T? {
    return T::class.java.cast(Fragment.instantiate(context, T::class.java.name, bundle))
}

inline fun <reified T : Fragment> Context.newFragment(bundle: Bundle? = null): T? {
    return T::class.java.cast(Fragment.instantiate(this, T::class.java.name, bundle))
}*/

inline fun <reified T : Activity> Fragment.getOwnActivity(): T? {
    activity ?: return null
    return T::class.java.cast(activity)
}
