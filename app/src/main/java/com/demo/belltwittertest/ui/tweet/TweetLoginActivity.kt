package com.demo.belltwittertest.ui.tweet

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.demo.belltwittertest.R
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.identity.TwitterLoginButton

class TweetLoginActivity : AppCompatActivity() {

    private lateinit var twitterLoginButton: TwitterLoginButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_twitter_login)
        twitterLoginButton = findViewById(R.id.twitterLogin)

        twitterLoginButton.callback = object : Callback<TwitterSession>() {
            override fun success(result: com.twitter.sdk.android.core.Result<TwitterSession>?) {
                result?.response?.code()?.let {
                    setResult(it)
                    finish()
                    Toast.makeText(applicationContext, R.string.successAuth, Toast.LENGTH_SHORT).show()
                }
            }

            override fun failure(e: TwitterException) {

                Toast.makeText(applicationContext, R.string.invalidUser, Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        twitterLoginButton.onActivityResult(requestCode, resultCode, data)
    }
}
