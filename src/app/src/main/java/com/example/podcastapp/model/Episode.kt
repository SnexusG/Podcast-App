package com.example.podcastapp.model

import java.util.*

data class Episode (
        var guid: String = "",
        var title: String = "",
        var description: String = "",
        var mediaUrl: String = "",
        var mimeType: String = "",
        var releaseDate: Date = Date(),
        var duration: String = ""
)

/*
* guid : Unique identifier provided in the RSS feed for an episode.
• title : The name of the episode.
• description : A description of the episode.
• mediaUrl : The location of the episode media. This is either an audio or video file.
• mimeType : Determines the type of file located at mediaUrl .
• releaseDate : Date the episode was released.
• duration : Duration of the episode as provided in the RSS feed.
*/