package com.demo.belltwittertest.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.demo.belltwittertest.utils.Const.DEFAULT_ZOOM_SCALE
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.LatLng
import com.twitter.sdk.android.core.models.Coordinates
import com.twitter.sdk.android.core.models.Tweet
import org.json.JSONObject

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

fun Tweet.getLatLng(): LatLng? {
    return (this.coordinates?.let { t -> LatLng(t.latitude, t.longitude) }
        ?: run {
            this.place?.boundingBox?.coordinates?.let { it ->
                if (it.isNotEmpty() && it[0].isNotEmpty()) LatLng(
                    it[0][0][Coordinates.INDEX_LATITUDE],
                    it[0][0][Coordinates.INDEX_LONGITUDE]
                )
                else null
            } ?: run { null }
        })
}

fun Tweet.toJSON(): JSONObject{
    val obj=JSONObject()
    obj.put("imgUrl",this.user.profileImageUrlHttps)
    obj.put("id",this.id)
    obj.put("text",this.text)
    obj.put("time",this.createdAt)
    return obj
}


