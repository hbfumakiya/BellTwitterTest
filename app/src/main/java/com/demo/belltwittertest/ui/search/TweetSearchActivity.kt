package com.demo.belltwittertest.ui.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.demo.belltwittertest.R

class TweetSearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_tweet_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.child_container, TweetSearchFragment.newInstance())
                .commitNow()
        }
    }
}