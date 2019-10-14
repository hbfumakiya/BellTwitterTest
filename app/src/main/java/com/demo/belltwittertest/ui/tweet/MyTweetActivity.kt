package com.demo.belltwittertest.ui.tweet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.demo.belltwittertest.R

class MyTweetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tweet_detail_activity)

        val id=intent?.getLongExtra("id",0)

        if (savedInstanceState == null) {
            val fragment=MyTweetFragment.newInstance()
            id?.let {
                val args=Bundle()
                args.putLong("id", it)
                fragment.arguments=args

                supportFragmentManager.beginTransaction()
                    .replace(R.id.child_container, fragment)
                    .commitNow()
            }

        }
    }
}