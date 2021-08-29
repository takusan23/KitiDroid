package io.github.takusan23.kitidroid.tool

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object PermissionCheckTool {

    /**
     * 位置情報の権限があるかどうか。あればtrueを返す
     * */
    fun isGrantedLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED

    }

}