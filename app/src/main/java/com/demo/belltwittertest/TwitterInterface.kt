package com.demo.belltwittertest

interface TwitterInterface{

    fun viewTweet(id:Long)

    fun favouriteTweet(id:Long)

    fun unfavouriteTweet(id:Long)

    fun reTweet(id:Long)

    fun undoReTweet(id:Long)


}