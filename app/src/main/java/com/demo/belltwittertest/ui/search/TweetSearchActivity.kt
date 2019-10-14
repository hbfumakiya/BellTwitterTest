package com.demo.belltwittertest.ui.search

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.demo.belltwittertest.R
import com.demo.belltwittertest.utils.PermissionValidator

class TweetSearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_tweet_activity)

        if(PermissionValidator.hasInternetAccess(this)){
            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.child_container, TweetSearchFragment.newInstance())
                    .commitNow()
            }
        }else{
            Toast.makeText(this,R.string.check_internet, Toast.LENGTH_SHORT).show()
        }

    }
}