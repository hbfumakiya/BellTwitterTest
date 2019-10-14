package com.demo.belltwittertest.ui.search.adapter

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.demo.belltwittertest.R
import com.demo.belltwittertest.TwitterInterface
import com.demo.belltwittertest.utils.loadUrl
import com.twitter.sdk.android.core.models.Tweet
import java.text.SimpleDateFormat
import java.util.*




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

    // this is viewholder for recyclerview
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

            setOnClickListener {
                twitInterface.viewTweet(item.id)
            }
        }

    }
}