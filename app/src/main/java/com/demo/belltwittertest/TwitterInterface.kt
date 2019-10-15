package com.demo.belltwittertest

/**
 * Created by Hardik on 2019-10-12.
 */
interface TwitterInterface{

    fun viewTweet(id:Long)

    fun favouriteTweet(id:Long)

    fun unfavouriteTweet(id:Long)

    fun reTweet(id:Long)

    fun undoReTweet(id:Long)


}