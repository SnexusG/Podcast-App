package com.example.podcastapp.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.podcastapp.getOrAwaitValue
import com.example.podcastapp.model.Episode
import com.example.podcastapp.model.Podcast
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class PodplayDatabaseTest {

    private lateinit var podcastDB : PodPlayDatabase
    private lateinit var podcastDBDao : PodcastDao
    private lateinit var date : Date

    @get:Rule
    var instanctTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        date = Date()
        podcastDB = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PodPlayDatabase::class.java
        ).allowMainThreadQueries().build()
        podcastDBDao = podcastDB.podcastDao()
    }

    @After
    fun tearDown(){
        podcastDB.close()
    }

    @Test
    fun EpisodeTest() = runBlocking {
        val episode = Episode("1", 1, "episode title", "episode description", "mediaURL", "mimeType", date, "duration")
        val episodesList:List<Episode> = listOf(episode)
        val podcast = Podcast(1, "some url", "some title", "some description", "image.jpeg", date, episodesList)
        podcastDBDao.insertPodcast(podcast)
        podcastDBDao.insertEpisode(episode)
        val loadpodcastEpisodes = podcastDBDao.loadEpisdoes(1)
        assertThat(loadpodcastEpisodes).contains(episode)
    }

    @Test
    fun deleteTest() = runBlocking{
        val episode = Episode("1", 1, "episode title", "episode description", "mediaURL", "mimeType", date, "duration")
        val episodesList:List<Episode> = listOf(episode)
        val podcast = Podcast(1, "some url", "some title", "some description", "image.jpeg", date, episodesList)
        podcastDBDao.insertPodcast(podcast)
        podcastDBDao.deletePodcast(podcast)
        val podcastC = podcastDBDao.loadPodcast().getOrAwaitValue()
        assertThat(podcastC).isEmpty()
    }
}
