package com.example.podcastapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.podcastapp.model.Episode
import com.example.podcastapp.model.Podcast

@Dao
interface PodcastDao{
    @Query("SELECT * FROM Podcast ORDER BY feedTitle")//change small f to big F if doesnt work with
    fun loadPodcast(): LiveData<List<Podcast>>

    @Query("SELECT * FROM Episode WHERE podcastId = :podcastId ORDER BY releaseDate DESC")
    fun loadEpisdoes(podcastId: Long): List<Episode>

    @Insert(onConflict = REPLACE)
    fun insertPodcast(podcast : Podcast): Long

    @Insert(onConflict = REPLACE)
    fun insertEpisode(edisode : Episode) : Long

}