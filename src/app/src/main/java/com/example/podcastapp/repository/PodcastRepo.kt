package com.example.podcastapp.repository

import androidx.lifecycle.LiveData
import com.example.podcastapp.Service.FeedService
import com.example.podcastapp.Service.RSSFeedResponse
import com.example.podcastapp.Service.RSSFeedService
import com.example.podcastapp.db.PodcastDao
import com.example.podcastapp.model.Episode
import com.example.podcastapp.model.Podcast
import com.example.podcastapp.util.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PodcastRepo(private val feedService: FeedService, private var podcastDao: PodcastDao) {
    fun getPodcast(feedUrl: String, callback: (Podcast?) -> Unit) {
        GlobalScope.launch {
            val podcast = podcastDao.loadPodcast(feedUrl)
            if (podcast != null) {
                podcast.id?.let {
                    podcast.episodes = podcastDao.loadEpisdoes(it)
                    GlobalScope.launch(Dispatchers.Main) {
                        callback(podcast)
                    }
                }
            } else {
                feedService.getFeed(feedUrl) { feedResponse ->
                    var podcast: Podcast? = null
                    if (feedResponse != null) {
                        podcast = rssResponseToPodcast(feedUrl, "", feedResponse)
                    }
                    GlobalScope.launch(Dispatchers.Main) {
                        callback(podcast)
                    }
                }
            }
        }
    }

    private fun rssItemsToEpisodes(episodeResponses: List<RSSFeedResponse.EpisodeResponse>): List<Episode> {
        return episodeResponses.map {
            Episode(
                    it.guid ?: "",
                    null,
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
        return Podcast(null, feedUrl, rssResponse.title, description, imageUrl,
                rssResponse.lastUpdated, episodes = rssItemsToEpisodes(items))
    }

        fun save(podcast:Podcast){
            GlobalScope.launch {
                val podcastId = podcastDao.insertPodcast(podcast)
                for (episode in podcast.episodes) {
                    episode.podcastId = podcastId
                    podcastDao.insertEpisode(episode)
                }
            }
        }

        fun getAll(): LiveData<List<Podcast>>{
            return podcastDao.loadPodcast()
        }

    fun delete(podcast : Podcast){
        GlobalScope.launch {
            podcastDao.deletePodcast(podcast)
        }
    }

}