package com.demo.belltwittertest.ui.main

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.demo.belltwittertest.R
import com.demo.belltwittertest.ui.search.TweetSearchActivity
import com.demo.belltwittertest.utils.PermissionValidator
import com.demo.belltwittertest.utils.PermissionValidator.PERMISSION_REQUEST

/**
 * Created by Hardik on 2019-10-12.
 * this is launcher activity for app
 */
class TweetMapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)
        if(PermissionValidator.allowedAllPermission(this)){
            if (savedInstanceState == null) {
                loadMainFragment()
            }
        }else {
            PermissionValidator.requestPermission(this)
        }
    }

    private fun loadMainFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, TweetMapFragment.newInstance())
            .commitNow()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==PERMISSION_REQUEST){
            if(grantResults.all { it== PackageManager.PERMISSION_GRANTED}){
                loadMainFragment()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
