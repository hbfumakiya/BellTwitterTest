package com.demo.belltwittertest.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.demo.belltwittertest.R
import com.demo.belltwittertest.ui.search.TweetSearchActivity
import com.demo.belltwittertest.utils.PermissionValidator


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PermissionValidator.requestPermission(this)

        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if(item.itemId==R.id.app_bar_search){
                val searchIntent= Intent(applicationContext,TweetSearchActivity::class.java)
                startActivity(searchIntent)
                return true
            }else{
                super.onOptionsItemSelected(item)

        }
    }

}
