package com.demo.belltwittertest.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.demo.belltwittertest.R
import com.demo.belltwittertest.ui.main.adapter.MyInfoViewAdapter.Companion.IMG_URL
import com.demo.belltwittertest.ui.main.adapter.MyInfoViewAdapter.Companion.TWEET_ID
import com.demo.belltwittertest.utils.Const.DEFAULT_ZOOM_SCALE
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.LatLng
import com.twitter.sdk.android.core.models.Coordinates
import com.twitter.sdk.android.core.models.Tweet
import org.json.JSONObject
/**
 * Created by Hardik on 2019-10-12.
 */
fun Activity.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
    return if (connectivityManager is ConnectivityManager) {
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        networkInfo?.isConnected ?: false
    } else false
}

fun Circle.getZoomLevel(): Float {
    var zoomLevel = DEFAULT_ZOOM_SCALE
    val radiusCircle = radius
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
    obj.put(IMG_URL,this.user.profileImageUrlHttps)
    obj.put(TWEET_ID,this.id)
    obj.put("text",this.text)
    obj.put("time",this.createdAt)
    return obj
}

fun ImageView.loadUrl(url: String?) {
    Glide.with(context)
        .load(url)
        .into(this)
}

fun ImageView.loadUrl(url: String?, placeholder: Int = R.drawable.tw__ic_logo_default) {
    Glide.with(context)
        .load(url)
        .placeholder(placeholder)
        .into(this)

}

fun Tweet.hasImage(): Boolean {
    extendedEntities?.media?.size?.let { return it == 1 && extendedEntities.media[0].type == "photo" }
    return false
}
fun Tweet.hasSingleImage(): Boolean {
    extendedEntities?.media?.size?.let { return it == 1 && extendedEntities.media[0].type == "photo" }
    return false
}

fun Tweet.hasSingleVideo(): Boolean {
    extendedEntities?.media?.size?.let { return it == 1 && extendedEntities.media[0].type != "photo" }
    return false
}

fun Tweet.hasMultipleMedia(): Boolean {
    extendedEntities?.media?.size?.let { return it > 1 }.run { return false }
}

fun Tweet.hasQuotedStatus(): Boolean {
    return quotedStatus != null
}

fun Tweet.hasLinks() : Boolean {
    return extendedEntities?.urls?.isNotEmpty() ?: false
}

fun Tweet.getImageUrl(): String {
    if (hasSingleImage() || hasMultipleMedia())
        return entities.media[0]?.mediaUrl ?: ""
    throw RuntimeException("No photo")
}

fun Tweet.getVideoCoverUrl(): String {
    if (hasSingleVideo() || hasMultipleMedia())
        return entities.media[0]?.mediaUrlHttps ?: (entities.media[0]?.mediaUrl ?: "")
    throw RuntimeException("No video")
}

fun Tweet.getVideoUrl(): Pair<String, String> {
    if (hasSingleVideo() || hasMultipleMedia()) {
        return Pair(extendedEntities.media[0].videoInfo.variants[0].url, extendedEntities.media[0].videoInfo.variants[0].contentType)
    }
    throw RuntimeException("No video")
}





