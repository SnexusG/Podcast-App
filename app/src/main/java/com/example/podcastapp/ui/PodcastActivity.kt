package com.example.podcastapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.podcastapp.R
import com.example.podcastapp.Service.ItunesService
import com.example.podcastapp.repository.ItunesRepo

class PodcastActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_podcast)

        val TAG = javaClass.simpleName
        val itunesService = ItunesService.instance
        val itunesRepo = ItunesRepo(itunesService)
        itunesRepo.searchByTerm("Android Developer", { Log.i(TAG,   "RESULTS : $it)")})
    }
}