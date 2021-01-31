package com.example.podcastapp.repository

import com.example.podcastapp.Service.FeedService
import com.example.podcastapp.Service.RSSFeedResponse
import com.example.podcastapp.Service.RSSFeedService
import com.example.podcastapp.model.Episode
import com.example.podcastapp.model.Podcast
import com.example.podcastapp.util.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PodcastRepo(private val feedService: FeedService) {
    fun getPodcast(feedUrl: String, callback: (Podcast?) -> Unit) {
        feedService.getFeed(feedUrl) { feedResponse ->
            var podcast:Podcast? = null
            if(feedResponse != null){
                podcast = rssResponseToPodcast(feedUrl, "",  feedResponse)
            }
            GlobalScope.launch(Dispatchers.Main){
                callback(podcast)
            }
        }
    }

    private fun rssItemsToEpisodes(episodeResponses: List<RSSFeedResponse.EpisodeResponse>): List<Episode> {
        return episodeResponses.map {
            Episode(
                    it.guid ?: "",
                    it.title ?: "",
                    it.description ?: "",
                    it.url ?: "",
                    it.type ?: "",
                    DateUtils.xmlDateToDate(it.pubDate),
                    it.duration ?: ""
            )
        }
    }

    private fun rssResponseToPodcast(feedUrl: String, imageUrl:
    String, rssResponse: RSSFeedResponse): Podcast? {

        val items = rssResponse.episodes ?: return null

        val description = if (rssResponse.description == "")
            rssResponse.summary else rssResponse.description
        return Podcast(feedUrl, rssResponse.title, description, imageUrl,
                rssResponse.lastUpdated, episodes = rssItemsToEpisodes(items))
    }

}