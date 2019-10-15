package com.demo.belltwittertest.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.demo.belltwittertest.R
import com.demo.belltwittertest.ui.search.adapter.TweetRecyclerAdapter.Companion.TYPE
import com.demo.belltwittertest.ui.search.adapter.TweetRecyclerAdapter.Companion.TYPE_IMAGE
import com.demo.belltwittertest.ui.search.adapter.TweetRecyclerAdapter.Companion.TYPE_VIDEO
import com.demo.belltwittertest.ui.search.adapter.TweetRecyclerAdapter.Companion.VIDEO_TYPE
import com.demo.belltwittertest.utils.isNetworkAvailable
import com.demo.belltwittertest.utils.loadUrl
import kotlinx.android.synthetic.main.media_activity.*
/**
 * Created by Hardik on 2019-10-12.
 * this activity displays image and videos on new activity
 */
class MediaViewActivity : AppCompatActivity(){

    companion object{
        const val ANIMATED_GIF="animated_gif"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.media_activity)
        if(isNetworkAvailable()){
            intent.extras?.let {
                if(intent.getStringExtra(TYPE)==TYPE_IMAGE){
                    imageView.visibility=View.VISIBLE
                    imageView.loadUrl(intent.getStringExtra(TYPE_IMAGE))

                }else if(intent.getStringExtra(TYPE)==TYPE_VIDEO){
                    mediaVideo.visibility= View.VISIBLE
                    val mc = MediaController(this)
                    mc.setAnchorView(mediaVideo)
                    mc.setMediaPlayer(mediaVideo)

                    if (intent.getStringExtra(VIDEO_TYPE) == ANIMATED_GIF)
                        mediaVideo.setOnCompletionListener { mediaVideo.start() }

                    mediaVideo.setMediaController(mc)
                    mediaVideo.setVideoURI(Uri.parse(intent.getStringExtra(TYPE_VIDEO)))
                    mediaVideo.requestFocus()
                    mediaVideo.start()
                }
            }
        }else{
            Toast.makeText(applicationContext,R.string.check_internet,Toast.LENGTH_SHORT).show()
            finish()
        }



    }
}
