package com.demo.belltwittertest.utils

import android.location.Location
import android.util.LruCache
import com.twitter.sdk.android.core.models.Tweet
import kotlinx.coroutines.*

object CacheLoader : Cache<String,Any> {

    private val lruCache:LruCache<String,Any> =LruCache(3)

    override fun getAsync(key: String): Deferred<Any?> {
       return GlobalScope.async {
            lruCache.get(key)
        }
    }

    override fun setAsync(key: String, value: Any): Deferred<Any> {
        return GlobalScope.async {
            lruCache.put(key,value)
        }
    }

    fun setRadius(radius:Double){
        runBlocking {
            val save = async(start = CoroutineStart.LAZY) {
                lruCache.put("radius",radius)
            }
            save.await()
        }
    }

    fun getRadius():Double{

        return runBlocking {
            val read = async(start = CoroutineStart.LAZY) {
                lruCache.get("radius") as Double
            }
           read.await()
        }
    }

    fun setLocation(location:Location){
        runBlocking {
            val save = async(start = CoroutineStart.LAZY) {
                lruCache.put("location",location)
            }
            save.await()
        }
    }

    fun getLocation():Location{
        return runBlocking {
            val read = async(start = CoroutineStart.LAZY) {
                lruCache.get("location") as Location
            }
            read.await()
        }
    }

    fun setRecentTweets(tweets:ArrayList<Tweet>){
        runBlocking {
            val save = async(start = CoroutineStart.LAZY) {
                lruCache.put("recentTweets",tweets)
            }
            save.await()
        }
    }

    fun getRecentTweets():ArrayList<Tweet>{
        return runBlocking {
            val read = async(start = CoroutineStart.LAZY) {
                lruCache.get("recentTweets") as ArrayList<Tweet>
            }
            read.await()
        }
    }
}

interface Cache<Key : Any, Value : Any> {
    fun getAsync(key: Key): Deferred<Value?>
    fun setAsync(key: Key, value: Value): Deferred<Any>
}



