package com.demo.belltwittertest.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.belltwittertest.R
import com.demo.belltwittertest.adapter.TweetRecyclerAdapter
import com.twitter.sdk.android.core.models.Tweet
import kotlinx.android.synthetic.main.search_tweet_fragment.*


class TweetSearchFragment:Fragment() {

    companion object {
        fun newInstance() = TweetSearchFragment()
    }

    private lateinit var tweetSearchviewModel: TweetSearchViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.search_tweet_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initUI()

        tweetSearchviewModel = ViewModelProviders.of(this).get(TweetSearchViewModel::class.java)

        tweetSearchviewModel.filteredTweets.observe(this, Observer {
            loadTweetsOnRecyclerView(it as ArrayList<Tweet>)
        })

        tweetSearchviewModel.filterTweets("")

    }

    private fun loadTweetsOnRecyclerView(list: ArrayList<Tweet>) {
        activity?.let {
            val layoutManager=LinearLayoutManager(it)
            recyclerView.layoutManager=layoutManager
            recyclerView.adapter=TweetRecyclerAdapter(it.applicationContext,list)
            recyclerView.setHasFixedSize(true)
          // recyclerView.addItemDecoration(DividerItemDecoration(it, DividerItemDecoration.VERTICAL))
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
}