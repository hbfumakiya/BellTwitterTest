package com.demo.belltwittertest.ui.tweet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.demo.belltwittertest.R
import com.demo.belltwittertest.TwitterInterface
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.models.Tweet

class MyTweetFragment:Fragment(),TwitterInterface {

    companion object {
        fun newInstance() = MyTweetFragment()
    }

    private var tweetId:Long=0

    private lateinit var tweetOpViewModel: TweetOperationViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.tweet_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tweetId=arguments?.get("id") as Long

        tweetOpViewModel = ViewModelProviders.of(this).get(TweetOperationViewModel::class.java)

        initUI()

        tweetOpViewModel.retrivedTweet.observe(this, Observer {
            displayTweet(it)
        })

    }

    private fun displayTweet(it: Tweet?) {
        Toast.makeText(activity,"${it?.text}",Toast.LENGTH_SHORT).show()

    }

    override fun viewTweet() {
        tweetOpViewModel.viewTweet(tweetId)
    }

    override fun favouriteTweet() {
        activity?.let {
            if(needTwitterSession()){
                startActivity(Intent(it,TweetLoginActivity::class.java))
            }else{
                tweetOpViewModel.favouriteTweet(tweetId)
            }
        }
    }

    override fun unfavouriteTweet() {
        activity?.let {
            if(needTwitterSession()){
                startActivity(Intent(it,TweetLoginActivity::class.java))
            }else{
                tweetOpViewModel.unFavouriteTweet(tweetId)
            }
        }

    }

    override fun reTweet() {
        activity?.let {
            if(needTwitterSession()){
                startActivity(Intent(it,TweetLoginActivity::class.java))
            }else{
                tweetOpViewModel.reTweet(tweetId)
            }
        }

    }

    override fun undoReTweet() {
        activity?.let {
            if(needTwitterSession()){
                startActivity(Intent(it,TweetLoginActivity::class.java))
            }else{
                tweetOpViewModel.undoReTweet(tweetId)
            }
        }

    }

    private fun needTwitterSession():Boolean=TwitterCore.getInstance().sessionManager.activeSession==null

    private fun initUI() {
        viewTweet()

    }
}