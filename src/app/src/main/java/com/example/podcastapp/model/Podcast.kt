package com.example.podcastapp.model

import java.util.*

data class Podcast(
        var feedUrl: String = "",
        var feedTitle: String = "",
        var feedDesc: String = "",
        var imageUrl: String = "",
        var lastUpdated: Date = Date(),
        var episodes: List<Episode> = listOf()
)

//• feedUrl : Location of the RSS feed.
//• feedTitle : Title of the podcast.
//• feedDesc : Description of the podcast.
//• imageUrl : Location of the podcast album art.
//• lastUpdated : Date the podcast was last updated.
//• episodes : List of episodes for the podcast.