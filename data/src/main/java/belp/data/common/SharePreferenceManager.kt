package belp.data.common

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

/**
 * Created by vophamtuananh on 3/13/18.
 */
class SharePreferenceManager(app: Context) {

    companion object {
        private const val SHARED_PREF_NAME = "com.myapplication.base"

        private const val KEY_ACCESS_TOKEN = "access_token"

    }

    private val sharedPreferences by lazy(LazyThreadSafetyMode.NONE) {
        app.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    var accessToken: String?
        get() = sharedPreferences.getString(KEY_ACCESS_TOKEN, "")
        set(value) = sharedPreferences.put { putString(KEY_ACCESS_TOKEN, value) }

    @SuppressLint("ApplySharedPref")
    private inline fun SharedPreferences.put(body: SharedPreferences.Editor.() -> Unit) {
        val editor = this.edit()
        editor.body()
        editor.apply()
    }

}