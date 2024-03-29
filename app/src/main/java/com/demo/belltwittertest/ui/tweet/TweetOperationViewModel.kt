package com.demo.belltwittertest.ui.tweet

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet
/**
 * Created by Hardik on 2019-10-12.
 */
class TweetOperationViewModel :ViewModel() {
    var retrivedTweet: MutableLiveData<Tweet> = MutableLiveData()

    fun getRetrivedTweet(): LiveData<Tweet> = retrivedTweet

    val callback=object : Callback<Tweet>() {
        override fun success(result: Result<Tweet>?) {

            val tweet=result?.response?.body() as Tweet
            Log.d("TweetOperationViewModel","operation successful")

            retrivedTweet.value=tweet
        }

        override fun failure(exception: TwitterException?) {

        }

    }

    fun viewTweet(id:Long) {
        TwitterCore.getInstance().apiClient.statusesService.show(id,null,null,null).enqueue(callback)
    }

    fun favouriteTweet(id:Long) {
        TwitterCore.getInstance().apiClient.favoriteService.create(id,null).enqueue(callback)

    }

    fun unFavouriteTweet(id:Long) {
        TwitterCore.getInstance().apiClient.favoriteService.destroy(id,null).enqueue(callback)
    }

    fun reTweet(id:Long) {
        TwitterCore.getInstance().apiClient.statusesService.retweet(id,null).enqueue(callback)
    }

    fun undoReTweet(id:Long) {
        TwitterCore.getInstance().apiClient.statusesService.unretweet(id,null).enqueue(callback)
    }

    fun needTwitterSession():Boolean= TwitterCore.getInstance().sessionManager.activeSession==null
}