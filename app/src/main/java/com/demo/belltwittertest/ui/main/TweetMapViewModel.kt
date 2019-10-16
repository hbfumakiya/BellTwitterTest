package com.demo.belltwittertest.ui.main

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demo.belltwittertest.utils.CacheLoader
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Search
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.core.services.params.Geocode

/**
 * Created by Hardik on 2019-10-12.
 * this is main activity viewmodel .making call to fetch latest tweets around radius
 */
class TweetMapViewModel : ViewModel() {

    private var tweetList:MutableLiveData<MutableList<Tweet>> = MutableLiveData()

    fun getTweetResult(): LiveData<MutableList<Tweet>> = tweetList

    fun getNearbyTweets(location: Location, radius:Float){

        try {
            CacheLoader.setLocation(location)

            CacheLoader.setRadius(radius.toDouble())

            val geoCode=Geocode(location.latitude,location.longitude,radius.toInt()/1000,Geocode.Distance.KILOMETERS)

            TwitterCore.getInstance().apiClient.searchService.tweets("#food",geoCode,null,null,
                null,100,null,null,null,true)
                .enqueue(object: Callback<Search>() {
                    override fun success(result: Result<Search>?) {
                        val tweets = (result?.response?.body() as Search).tweets
                        tweetList.postValue(tweets.toMutableList())

                        CacheLoader.setRecentTweets(tweets)
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
