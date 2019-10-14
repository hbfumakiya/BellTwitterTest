package com.demo.belltwittertest.ui.main.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.demo.belltwittertest.R
import com.demo.belltwittertest.ui.tweet.TweetDetailActivity
import com.demo.belltwittertest.utils.loadUrl
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.Marker
import org.json.JSONObject

class MyInfoViewAdapter(private val activity: Activity): InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {


    override fun getInfoContents(marker: Marker?): View {
       val view=LayoutInflater.from(activity).inflate(R.layout.info_marker_view,null)
        val data=getMarkerData(marker)
        val imgUrl= data.getString("imgUrl")

        view.findViewById<TextView>(R.id.title).text= marker?.title

        val imageView=view.findViewById<ImageView>(R.id.img)

//        Glide.with(activity)
//            .load(imgUrl)
//            .into(imageView)
        imageView.loadUrl(imgUrl)

        return view
    }

    override fun onInfoWindowClick(p0: Marker?)
    {
        val data = getMarkerData(p0)
        val id= data.getLong("id")

        val detailTweet= Intent(activity, TweetDetailActivity::class.java)
        detailTweet.putExtra("id",id)
        activity.startActivity(detailTweet)
    }

    override fun getInfoWindow(marker: Marker?) =null

    private fun getMarkerData(marker: Marker?): JSONObject {
        marker?.snippet?.let {
            return JSONObject(it)
        }
        return JSONObject()

    }
}