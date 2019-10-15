package com.demo.belltwittertest.ui.search.adapter

import android.content.Context
import android.content.Intent
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.demo.belltwittertest.R
import com.demo.belltwittertest.TwitterInterface
import com.demo.belltwittertest.ui.MediaViewActivity
import com.demo.belltwittertest.utils.*
import com.twitter.sdk.android.core.models.Tweet
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Hardik on 2019-10-12.
 * this is recycler view adapter for tweet items
 */
class TweetRecyclerAdapter (private val context: Context, private var tweets: ArrayList<Tweet>,
                            private val twitInterface:TwitterInterface) :  RecyclerView.Adapter<TweetRecyclerAdapter.TweetViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetViewHolder {

        val itemView = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false)

        return TweetViewHolder(itemView)
    }


    override fun getItemCount(): Int {
        return tweets.size
    }

    override fun onBindViewHolder(holder: TweetViewHolder, position: Int) {
        holder.bind(tweets[position])
    }

    private fun setItems(updatedTweets: ArrayList<Tweet>) {
        this.tweets = updatedTweets
        notifyDataSetChanged()
    }

    fun updateTweet(tweet: Tweet) {
        tweets.forEach { t->
            if(t.id==tweet.id){
                tweets.remove(t)
                tweets.add(tweet)
                setItems(tweets)
                return
            }
        }
    }

    inner class TweetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Tweet) = with(itemView) {
            with(itemView.findViewById<ImageView>(R.id.profileView)){
                loadUrl(item.user.profileImageUrl)
            }
            with(itemView.findViewById<TextView>(R.id.userName)){
                text=item.user.name
            }
            with(itemView.findViewById<TextView>(R.id.userId)){
                text=item.user.screenName
            }
            with(itemView.findViewById<TextView>(R.id.text)) {
                text=item.text.trim()
            }
            with(itemView.findViewById<TextView>(R.id.favCount)) {
                text=item.favoriteCount.toString()
            }
            with(itemView.findViewById<ImageView>(R.id.favImg)) {
                if(item.favorited)
                    setImageResource(R.drawable.favourite_filled)
                else
                    setImageResource(R.drawable.favourite_border)

                setOnClickListener {
                    if(item.favorited)
                        twitInterface.unfavouriteTweet(item.id)
                    else
                        twitInterface.favouriteTweet(item.id)

                }
            }
            with(itemView.findViewById<TextView>(R.id.retweetCount)) {
                text=item.retweetCount.toString()
            }
            with(itemView.findViewById<ImageView>(R.id.retweetImg)) {
                if(item.retweeted)
                    setImageResource(R.drawable.retweet_select)
                else
                    setImageResource(R.drawable.retweet_unselect)
                setOnClickListener {
                    if(item.retweeted)
                        twitInterface.undoReTweet(item.id)
                    else
                        twitInterface.reTweet(item.id)

                }
            }
            with(itemView.findViewById<TextView>(R.id.timeStamp)) {
                val timestamp=SimpleDateFormat("EEE MMM dd HH:mm:ss Zyyyy", Locale.ENGLISH).parse(item.createdAt).time
                timestamp.let {
                    text=DateUtils.getRelativeTimeSpanString(context,timestamp)
                }
            }
            with(itemView.findViewById<ImageView>(R.id.mediaImg)) {
                when {
                    item.hasImage() -> {
                        visibility=View.VISIBLE
                        loadUrl(item.getImageUrl())
                        setOnClickListener{
                            val intent =  Intent(context, MediaViewActivity::class.java)
                            intent.putExtra(TYPE, TYPE_IMAGE)
                            intent.putExtra(TYPE_IMAGE, item.getImageUrl())
                            context.startActivity(intent)
                        }
                    }
                    item.hasSingleVideo() -> {
                        visibility=View.VISIBLE
                        loadUrl(item.getVideoCoverUrl())
                        setOnClickListener {
                            val pair=item.getVideoUrl()
                            val intent =  Intent(context, MediaViewActivity::class.java)
                            intent.putExtra(TYPE, TYPE_VIDEO)
                            intent.putExtra(TYPE_VIDEO, pair.first)
                            intent.putExtra(VIDEO_TYPE, pair.second)
                            context.startActivity(intent)
                        }

                    }
                    else -> visibility=View.GONE
                }

            }

            setOnClickListener {
                twitInterface.viewTweet(item.id)
            }
        }

    }
    companion object{
        const val TYPE="type"
        const val TYPE_IMAGE="image"
        const val TYPE_VIDEO="video"
        const val VIDEO_TYPE="videoType"

    }
}