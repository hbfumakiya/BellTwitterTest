package com.demo.belltwittertest.ui.main

import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Search
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.services.params.Geocode


class MainViewModel : ViewModel() {

    var tweetList:MutableLiveData<MutableList<Tweet>> = MutableLiveData()

    fun getNearbyTweets(location: Location, radius:Float){

        try {
            val geoCode=Geocode(location.latitude,location.longitude,radius.toInt(),Geocode.Distance.KILOMETERS)
            TwitterCore.getInstance().apiClient.searchService.tweets("#food",geoCode,null,null,
                null,100,null,null,null,true)
                .enqueue(object: Callback<Search>() {
                    override fun success(result: Result<Search>?) {
                        val tweets = (result?.response?.body() as Search).tweets
                        tweetList.value=tweets.toMutableList()
                    }

                    override fun failure(exception: TwitterException?) {
                       exception?.printStackTrace()
                    }

                })

        }catch (e:Exception){
            e.printStackTrace()
        }


    }

}
