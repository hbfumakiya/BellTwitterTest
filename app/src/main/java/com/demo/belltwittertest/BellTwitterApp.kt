package com.demo.belltwittertest

import android.app.Application
import android.util.Log
import com.demo.belltwittertest.utils.Const
import com.twitter.sdk.android.core.DefaultLogger
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig

/**
 * Created by Hardik on 2019-10-12.
 */
class BellTwitterApp :Application() {
    override fun onCreate() {
        super.onCreate()
       val twitterConfig= configureTwitterInstance()

        Twitter.initialize(twitterConfig)
    }

    private fun configureTwitterInstance()= TwitterConfig.Builder(this)
                                                .logger(DefaultLogger(Log.DEBUG))
                                                .twitterAuthConfig(TwitterAuthConfig(Const.TWITTER_CONSUMER_API_KEY,Const.TWITTER_CONSUMER_API_SECRET_KEY))
                                                .debug(true)
                                                .build()

}