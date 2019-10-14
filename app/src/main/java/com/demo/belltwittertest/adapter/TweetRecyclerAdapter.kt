package com.demo.belltwittertest.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.demo.belltwittertest.R
import com.demo.belltwittertest.ui.tweet.MyTweetActivity
import com.twitter.sdk.android.core.models.Tweet

class TweetRecyclerAdapter (private val context: Context, private val tweets: ArrayList<Tweet>) :  RecyclerView.Adapter<TweetRecyclerAdapter.TweetViewHolder>() {
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

    // this is viewholder for recyclerview
    inner class TweetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Tweet) = with(itemView) {
            with(itemView.findViewById<TextView>(R.id.textView)) {
                text=item.text
            }
            setOnClickListener {
                val detailTweet= Intent(context, MyTweetActivity::class.java)
                detailTweet.putExtra("id",item.getId())
                detailTweet.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(detailTweet)
            }

        }

    }
}