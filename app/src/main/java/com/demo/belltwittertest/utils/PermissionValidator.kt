package com.demo.belltwittertest.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Created by Hardik on 2019-10-12.
 */
object PermissionValidator {

    const val PERMISSION_REQUEST=2000

    fun requestPermission(activity:Activity){
        ActivityCompat.requestPermissions(activity,
            arrayOf(Manifest.permission.INTERNET,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST)
    }

    fun hasLocationAccess(context: Context):Boolean{
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED
    }

    fun hasInternetAccess(context: Context):Boolean{
        return ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET)==PackageManager.PERMISSION_GRANTED
    }

    fun allowedAllPermission(context: Context):Boolean{
        var allowed=true
        arrayOf(Manifest.permission.INTERNET,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION).forEach {
            if(ContextCompat.checkSelfPermission(context, it)== PackageManager.PERMISSION_GRANTED)
                allowed=false
        }
        return allowed
    }
}