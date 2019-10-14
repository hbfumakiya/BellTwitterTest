package com.demo.belltwittertest.ui.tweet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.demo.belltwittertest.R

class TweetDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tweet_detail_activity)

        if (savedInstanceState == null) {
            intent.extras.let {
                val fragment=TweetDetailFragment.newInstance()
                fragment.arguments= intent.extras

                supportFragmentManager.beginTransaction()
                    .replace(R.id.child_container, fragment)
                    .commitNow()
            }

        }
    }
}