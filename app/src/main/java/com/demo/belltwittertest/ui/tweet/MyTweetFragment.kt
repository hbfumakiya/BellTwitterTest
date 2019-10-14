package com.demo.belltwittertest.ui.tweet

import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.demo.belltwittertest.R
import com.demo.belltwittertest.TwitterInterface
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.models.Tweet
import kotlinx.android.synthetic.main.media_layout.*
import kotlinx.android.synthetic.main.tweet_detail.*
import kotlinx.android.synthetic.main.tweet_detail_fragment.*
import java.text.SimpleDateFormat
import java.util.*

class MyTweetFragment:Fragment(),TwitterInterface {

    companion object {
        fun newInstance() = MyTweetFragment()
    }

    private var tweetId:Long=0

    private lateinit var tweetOpViewModel: TweetOperationViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view= inflater.inflate(R.layout.tweet_detail_fragment, container, false);
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tweetId=arguments?.get("id") as Long

        tweetOpViewModel = ViewModelProviders.of(this).get(TweetOperationViewModel::class.java)

        tweetOpViewModel.retrivedTweet.observe(this, Observer {
            displayTweet(it)
        })

        viewTweet(tweetId)

    }

    private fun displayTweet(tweet: Tweet?) {
        if(tweet!=null){
            profileView.apply {
                setImageResource(R.drawable.tw__ic_logo_default)
                setBackgroundColor(context.resources.getColor(R.color.colorPrimary))
            }
            userName.apply {
                text=tweet.user.name
            }
            userId.apply {
                text=tweet.user.screenName
            }
            text.apply {
                text=tweet.text.trim()
            }
            favCount.apply {
                text=tweet.favoriteCount.toString()
            }
            favImg.apply {
                if(tweet.favorited)
                    setImageResource(R.drawable.favourite_filled)
                else
                    setImageResource(R.drawable.favourite_border)

                setOnClickListener {
                    if(tweet.favorited)
                        unfavouriteTweet(tweet.id)
                    else
                        favouriteTweet(tweet.id)
                }

            }
            retweetCount.apply {
                text=tweet.retweetCount.toString()
            }
            retweetImg.apply {
                if(tweet.retweeted)
                    setImageResource(R.drawable.retweet_select)
                else
                    setImageResource(R.drawable.retweet_unselect)

                setOnClickListener {
                    if(tweet.retweeted)
                        undoReTweet(tweet.id)
                    else
                        reTweet(tweet.id)
                }

            }
            timeStamp.apply {
                val timestamp= SimpleDateFormat("EEE MMM dd HH:mm:ss Zyyyy", Locale.ENGLISH).parse(tweet.createdAt).time
                timestamp.let {
                    text= DateUtils.getRelativeTimeSpanString(context,timestamp)
                }
            }
            mediaImg.apply {
                if(tweet.extendedEntities.media.size>0 && tweet.extendedEntities.media[0].type=="photo"){
                    visibility=View.VISIBLE
                    activity?.applicationContext?.let { Glide.with(context).load(tweet.extendedEntities.media[0].mediaUrl).into(this); }
                }else{
                    visibility=View.GONE
                }

            }


            Toast.makeText(activity,"${tweet?.text}",Toast.LENGTH_SHORT).show()
        }



    }

    override fun viewTweet(id:Long) {
        tweetOpViewModel.viewTweet(tweetId)
    }

    override fun favouriteTweet(id:Long) {
        activity?.let {
            if(needTwitterSession()){
                startActivity(Intent(it,TweetLoginActivity::class.java))
            }else{
                tweetOpViewModel.favouriteTweet(tweetId)
            }
        }
    }

    override fun unfavouriteTweet(id:Long) {
        activity?.let {
            if(needTwitterSession()){
                startActivity(Intent(it,TweetLoginActivity::class.java))
            }else{
                tweetOpViewModel.unFavouriteTweet(tweetId)
            }
        }

    }

    override fun reTweet(id:Long) {
        activity?.let {
            if(needTwitterSession()){
                startActivity(Intent(it,TweetLoginActivity::class.java))
            }else{
                tweetOpViewModel.reTweet(tweetId)
            }
        }

    }

    override fun undoReTweet(id:Long) {
        activity?.let {
            if(needTwitterSession()){
                startActivity(Intent(it,TweetLoginActivity::class.java))
            }else{
                tweetOpViewModel.undoReTweet(tweetId)
            }
        }

    }

    private fun needTwitterSession():Boolean= TwitterCore.getInstance().sessionManager.activeSession==null
}