package com.demo.belltwittertest.ui.tweet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.demo.belltwittertest.R
import com.demo.belltwittertest.TwitterInterface
import com.demo.belltwittertest.ui.MediaViewActivity
import com.demo.belltwittertest.utils.*
import com.twitter.sdk.android.core.models.Tweet
import kotlinx.android.synthetic.main.media_layout.*
import kotlinx.android.synthetic.main.tweet_detail.*
import kotlinx.android.synthetic.main.tweet_detail_fragment.*
import java.text.SimpleDateFormat
import java.util.*

class TweetDetailFragment:Fragment(),TwitterInterface {

    companion object {
        fun newInstance() = TweetDetailFragment()
    }

    private var tweetId:Long=0

    private lateinit var tweetOpViewModel: TweetOperationViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.tweet_detail_fragment, container, false)
    }

    private fun openTweetInApp(tweet:Tweet) {
        if(tweetId!=0L){
            activity?.let {context->
                val intent=Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/${tweet.user.screenName}/status/${tweet.id}"))
                context.startActivity(Intent.createChooser(intent,"Tweet"))
            }
        }
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
        tweet?.let { tweet->
            view?.setOnClickListener {
                openTweetInApp(tweet)
            }
            profileView.loadUrl(tweet.user.profileImageUrl)

            userName.text=tweet.user.name

            userId.text=tweet.user.screenName

            text.text=tweet.text.trim()

            favCount.text=tweet.favoriteCount.toString()

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
            retweetCount.text=tweet.retweetCount.toString()

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
            val timeString= SimpleDateFormat("EEE MMM dd HH:mm:ss Zyyyy", Locale.ENGLISH).parse(tweet.createdAt).time
            timeStamp.text= DateUtils.getRelativeTimeSpanString(context,timeString)

            mediaImg.apply {
                when {
                    tweet.hasImage() -> {
                        visibility=View.VISIBLE
                        loadUrl(tweet.getImageUrl())
                        setOnClickListener{
                            val intent =  Intent(context, MediaViewActivity::class.java)
                            intent.putExtra("type", "image")
                            intent.putExtra("image", tweet.getImageUrl())
                            context.startActivity(intent)
                        }
                    }
                    tweet.hasSingleVideo() -> {
                        visibility=View.VISIBLE
                        loadUrl(tweet.getVideoCoverUrl())
                        setOnClickListener {
                            val pair=tweet.getVideoUrl()
                            val intent =  Intent(context, MediaViewActivity::class.java)
                            intent.putExtra("type", "video")
                            intent.putExtra("video", pair.first)
                            intent.putExtra("videoType", pair.second)
                            context.startActivity(intent)
                        }

                    }
                    else -> visibility=View.GONE
                }

                if(tweet.hasImage()){
                    visibility=View.VISIBLE
                    loadUrl(tweet.getImageUrl())
                }else{
                    visibility=View.GONE
                }

            }

        }




    }

    override fun viewTweet(id:Long) {
        tweetOpViewModel.viewTweet(tweetId)
    }

    override fun favouriteTweet(id:Long) {
        activity?.let {
            if(tweetOpViewModel.needTwitterSession()){
                startActivity(Intent(it,TweetLoginActivity::class.java))
            }else{
                tweetOpViewModel.favouriteTweet(tweetId)
            }
        }
    }

    override fun unfavouriteTweet(id:Long) {
        activity?.let {
            if(tweetOpViewModel.needTwitterSession()){
                startActivity(Intent(it,TweetLoginActivity::class.java))
            }else{
                tweetOpViewModel.unFavouriteTweet(tweetId)
            }
        }

    }

    override fun reTweet(id:Long) {
        activity?.let {
            if(tweetOpViewModel.needTwitterSession()){
                startActivity(Intent(it,TweetLoginActivity::class.java))
            }else{
                tweetOpViewModel.reTweet(tweetId)
            }
        }

    }

    override fun undoReTweet(id:Long) {
        activity?.let {
            if(tweetOpViewModel.needTwitterSession()){
                startActivity(Intent(it,TweetLoginActivity::class.java))
            }else{
                tweetOpViewModel.undoReTweet(tweetId)
            }
        }

    }


}