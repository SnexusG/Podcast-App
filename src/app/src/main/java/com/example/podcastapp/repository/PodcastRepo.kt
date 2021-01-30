package com.example.podcastapp.repository

import com.example.podcastapp.model.Podcast

class PodcastRepo{
    fun getPodcast(feedUrl : String, callback : (Podcast?) ->Unit){
        callback(Podcast(feedUrl, "No Name", "No Description", "No Image"))
    }
}