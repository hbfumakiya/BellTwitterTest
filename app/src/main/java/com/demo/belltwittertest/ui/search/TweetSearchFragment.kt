package com.demo.belltwittertest.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.belltwittertest.R
import com.demo.belltwittertest.TwitterInterface
import com.demo.belltwittertest.adapter.TweetRecyclerAdapter
import com.demo.belltwittertest.ui.tweet.MyTweetActivity
import com.demo.belltwittertest.ui.tweet.TweetLoginActivity
import com.demo.belltwittertest.ui.tweet.TweetOperationViewModel
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.models.Tweet
import kotlinx.android.synthetic.main.search_tweet_fragment.*


class TweetSearchFragment:Fragment(),TwitterInterface {


    companion object {
        fun newInstance() = TweetSearchFragment()
    }

    private lateinit var tweetSearchviewModel: TweetSearchViewModel
    private lateinit var tweetOpViewModel: TweetOperationViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.search_tweet_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initUI()

        tweetSearchviewModel = ViewModelProviders.of(this).get(TweetSearchViewModel::class.java)
        tweetOpViewModel = ViewModelProviders.of(this).get(TweetOperationViewModel::class.java)

        tweetSearchviewModel.filteredTweets.observe(this, Observer {
            loadTweetsOnRecyclerView(it as ArrayList<Tweet>)
        })

        tweetSearchviewModel.filterTweets("")

    }

    private fun loadTweetsOnRecyclerView(list: ArrayList<Tweet>) {
        activity?.let {
            val layoutManager=LinearLayoutManager(it)
            recyclerView.layoutManager=layoutManager
            recyclerView.adapter=TweetRecyclerAdapter(it,list,this)
            recyclerView.setHasFixedSize(true)
        }

    }

    private fun initUI() {
        searchView.doOnLayout {
            searchView.requestFocus()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                tweetSearchviewModel.filterTweets(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

        })

    }

    private fun needTwitterSession():Boolean= TwitterCore.getInstance().sessionManager.activeSession==null

    override fun favouriteTweet(id:Long) {
        activity?.let {
            if(needTwitterSession()){
                startActivity(Intent(it, TweetLoginActivity::class.java))
            }else{
                tweetOpViewModel.favouriteTweet(id)
            }
        }
    }

    override fun unfavouriteTweet(id:Long) {
        activity?.let {
            if(needTwitterSession()){
                startActivity(Intent(it, TweetLoginActivity::class.java))
            }else{
                tweetOpViewModel.unFavouriteTweet(id)
            }
        }

    }

    override fun reTweet(id:Long) {
        activity?.let {
            if(needTwitterSession()){
                startActivity(Intent(it, TweetLoginActivity::class.java))
            }else{
                tweetOpViewModel.reTweet(id)
            }
        }

    }

    override fun undoReTweet(id:Long) {
        activity?.let {
            if(needTwitterSession()){
                startActivity(Intent(it, TweetLoginActivity::class.java))
            }else{
                tweetOpViewModel.undoReTweet(id)
            }
        }

    }
    override fun viewTweet(id:Long) {
        activity?.let {
            val detailTweet= Intent(activity, MyTweetActivity::class.java)
            detailTweet.putExtra("id",id)
            detailTweet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            it.startActivity(detailTweet)
        }


    }



}