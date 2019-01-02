package com.base.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

/**
 * Created by vophamtuananh on 1/7/18.
 */
class DeviceUtil {

    companion object {
        const val PERMISSION_CAMERA_REQUEST_CODE = 1001
        const val PERMISSION_READ_EXTERNAL_REQUEST_CODE = 1002
        const val PERMISSION_CALL_PHONE_REQUEST_CODE = 1003
        const val PERMISSION_WRITE_STORAGE_REQUEST_CODE = 1004
        const val PERMISSION_LOCATION_REQUEST_CODE = 1005

        const val CAMERA_REQUEST_CODE = 1011
        const val GALLERY_REQUEST_CODE = 1012

        fun checkWriteStoragePermission(context: Context): Boolean {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(context,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
            return true
        }

        private fun checkLocationPermission(context: Context): Boolean {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(context,
                                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
            return true
        }

        fun requestLocationPermission(activity: Activity): Boolean {
            if (!checkLocationPermission(activity)) {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_LOCATION_REQUEST_CODE)
                return false
            }
            return true
        }

        fun checkPhoneStatePermission(context: Context): Boolean {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
            return true
        }

        fun checkCameraPermission(context: Context): Boolean {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
            return true
        }

        private fun checkReadStoragePermision(context: Context): Boolean {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
            return true
        }

        private fun checkPhonePermission(context: Context): Boolean {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
            return true
        }

        fun openCamera(activity: Activity, fileName: String?): String? {
            if (checkCameraPermission(activity.applicationContext)) {
                val tempFile = FileUtil.getOutputMediaFile(activity, fileName)
                tempFile?.let {
                    camera(activity, it)
                    return tempFile.absolutePath
                }
            } else {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_CAMERA_REQUEST_CODE)
            }
            return null
        }

        private fun camera(activity: Activity, tempFile: File) {
            val capturedFileUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(activity,
                        activity.applicationContext.packageName + ".provider", tempFile)
            } else {
                Uri.fromFile(tempFile)
            }
            val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePhotoIntent.resolveActivity(activity.packageManager) != null) {
                takePhotoIntent.putExtra("return-data", true)
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedFileUri)
                val chooserIntent = Intent.createChooser(takePhotoIntent, "Selection Photo")
                if (chooserIntent != null)
                    activity.startActivityForResult(chooserIntent, CAMERA_REQUEST_CODE)
            }
        }

        fun openGallery(activity: Activity) {
            if (checkReadStoragePermision(activity.applicationContext)) {
                gallery(activity)
            } else {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_READ_EXTERNAL_REQUEST_CODE)
            }
        }

        private fun gallery(activity: Activity) {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            if (photoPickerIntent.resolveActivity(activity.packageManager) != null) {
                photoPickerIntent.type = "image/*"
                val chooserIntent = Intent.createChooser(photoPickerIntent, "Selection Photo")
                if (chooserIntent != null)
                    activity.startActivityForResult(chooserIntent, GALLERY_REQUEST_CODE)
            }
        }

        @SuppressLint("MissingPermission")
        fun callToPhoneNumber(activity: Activity, phoneNumber: String) {
            if (checkPhonePermission(activity.applicationContext)) {
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber))
                activity.startActivity(intent)
            } else {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CALL_PHONE), PERMISSION_CALL_PHONE_REQUEST_CODE)
            }
        }
    }

}