package com.demo.belltwittertest.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.demo.belltwittertest.utils.Const.DEFAULT_ZOOM_SCALE
import com.google.android.gms.maps.model.Circle

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
    return if (connectivityManager is ConnectivityManager) {
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        networkInfo?.isConnected ?: false
    } else false
}

fun getZoomLevel(circle: Circle?): Float {
    var zoomLevel = DEFAULT_ZOOM_SCALE
    val radiusCircle = circle?.radius ?: (CacheLoader.getRadius())
    val radius = radiusCircle + radiusCircle / 2
    val scale = radius / 500
    zoomLevel = ((16 - Math.log(scale) / Math.log(2.0)).toFloat())
    return zoomLevel + 0.4F
}