package com.example.podcastapp.Service

import okhttp3.*
import java.io.IOException

class RSSFeedService : FeedService{
    override fun getFeed(xmlFileURL: String, callback: (RSSFeedResponse?) -> Unit) {
        val client = OkHttpClient()
        val request = Request.Builder()
                .url(xmlFileURL)
                .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException){
                callback(null)
            }
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response){
                if(response.isSuccessful){
                    response.body()?.let{ responseBody ->
                        println(responseBody.string())
                        return
                    }
                }
                callback(null)
            }

        })
    }
}

interface FeedService {
    fun getFeed(xmlFileURL : String, callback : (RSSFeedResponse?) -> Unit)
        companion object{
            val instance : FeedService by lazy{
                RSSFeedService()
            }
        }

}