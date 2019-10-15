package com.demo.belltwittertest.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.demo.belltwittertest.R
import com.demo.belltwittertest.utils.isNetworkAvailable
import com.demo.belltwittertest.utils.loadUrl
import kotlinx.android.synthetic.main.media_activity.*

class MediaViewActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.media_activity)
        if(isNetworkAvailable()){
            intent.extras?.let {
                if(intent.getStringExtra("type")=="image"){
                    imageView.visibility=View.VISIBLE
                    imageView.loadUrl(intent.getStringExtra("image"))

                }else if(intent.getStringExtra("type")=="video"){
                    mediaVideo.visibility= View.VISIBLE
                    val mc = MediaController(this)
                    mc.setAnchorView(mediaVideo)
                    mc.setMediaPlayer(mediaVideo)

                    if (intent.getStringExtra("videoType") == "animated_gif")
                        mediaVideo.setOnCompletionListener { mediaVideo.start() }

                    mediaVideo.setMediaController(mc)
                    mediaVideo.setVideoURI(Uri.parse(intent.getStringExtra("video")))
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
