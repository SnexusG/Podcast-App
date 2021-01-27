package com.example.podcastapp.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import com.example.podcastapp.R
import com.example.podcastapp.Service.ItunesService
import com.example.podcastapp.repository.ItunesRepo

class PodcastActivity : AppCompatActivity() {
    val TAG = javaClass.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_podcast)


//        val itunesService = ItunesService.instance
//        val itunesRepo = ItunesRepo(itunesService)
//        itunesRepo.searchByTerm("Android Developer") { Log.i(TAG,   "RESULTS : $it")}

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_search, menu)

        val searchMenuItem = menu.findItem(R.id.search_item)
        val searchView = searchMenuItem?.actionView as SearchView

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        return true
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent : Intent) {
        if(intent.action == Intent.ACTION_SEARCH){
            val query = intent.getStringExtra(SearchManager.QUERY)
            performSearch(query.toString())
        }
    }

    private fun performSearch(term: String){
        val itunesService = ItunesService.instance
        val itunesRepo = ItunesRepo(itunesService)
        itunesRepo.searchByTerm(term){
            Log.i(TAG, "RESULTS : $it")
        }
    }
}