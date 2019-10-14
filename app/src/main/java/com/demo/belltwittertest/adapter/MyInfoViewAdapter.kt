package com.demo.belltwittertest.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.demo.belltwittertest.R
import com.demo.belltwittertest.ui.tweet.MyTweetActivity
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.Marker
import org.json.JSONObject

class MyInfoViewAdapter(private val activity: Activity): InfoWindowAdapter {

    override fun getInfoContents(marker: Marker?): View {
       val view=LayoutInflater.from(activity).inflate(R.layout.info_marker_view,null)
        val data=getMarkerData(marker)
        val imgUrl= data.getString("imgUrl")

        view.findViewById<TextView>(R.id.title).text= marker?.title

        val imageView=view.findViewById<ImageView>(R.id.img)

        Glide.with(activity)
            .load(imgUrl)
            .into(imageView)

        view.setOnClickListener{
             val id= data.getLong("id")

            val detailTweet= Intent(activity.applicationContext, MyTweetActivity::class.java)
            detailTweet.putExtra("id",id)
            detailTweet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.applicationContext.startActivity(detailTweet)
        }

        return view
    }

    override fun getInfoWindow(marker: Marker?) =null

    private fun getMarkerData(marker: Marker?): JSONObject {
        marker?.snippet?.let {
            return JSONObject(it)
        }
        return JSONObject()

    }
}