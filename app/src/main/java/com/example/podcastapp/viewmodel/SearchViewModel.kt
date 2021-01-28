package com.example.podcastapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.podcastapp.Service.PodcastResponse
import com.example.podcastapp.repository.ItunesRepo

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    var itunesRepo: ItunesRepo? = null

    data class PodcastSummaryViewData(val name: String? = "", val lastUpdated: String? = "", val imageUrl: String? = "", val feedUrl: String? = "")

    private fun itunesPodcastToPodcastSummaryView(itunesPodcast: PodcastResponse.ItunesPodcast) : PodcastSummaryViewData{
        return PodcastSummaryViewData(itunesPodcast.collectionCensoredName, itunesPodcast.releaseDate, itunesPodcast.artworkUrl30, itunesPodcast.feedUrl)
    }

    fun searchPodcast(term: String, callback:(List<PodcastSummaryViewData>) -> Unit){
        itunesRepo?.searchByTerm(term) { results ->
            if(results == null){
                callback(emptyList())
            }else{
                val searchViews = results.map{podcast -> itunesPodcastToPodcastSummaryView(podcast)}
                callback(searchViews)
            }
        }
    }
}