package com.demo.belltwittertest.ui.search

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
 * this is main view model for search nearby tweets
 */
class TweetSearchViewModel :ViewModel() {
    var filteredTweets: MutableLiveData<MutableList<Tweet>> = MutableLiveData()

    fun getFilteredTweets(): LiveData<MutableList<Tweet>> = filteredTweets

    fun filterTweets(filterText:String){

        try {
            val location= CacheLoader.getLocation()
            val radius=CacheLoader.getRadius()
            location?.let {
                val geoCode= Geocode(location.latitude,location.longitude,radius.toInt()/1000, Geocode.Distance.KILOMETERS)
                TwitterCore.getInstance().apiClient.searchService.tweets(filterText,geoCode,null,null,
                    null,100,null,null,null,true)
                    .enqueue(object: Callback<Search>() {
                        override fun success(result: Result<Search>?) {
                            val tweets = (result?.response?.body() as Search).tweets?: emptyList()
                            filteredTweets.value=tweets.toMutableList()
                        }

                        override fun failure(exception: TwitterException?) {
                            exception?.printStackTrace()
                        }

                    })
            }


        }catch (e:Exception){
            e.printStackTrace()
        }

    }
}