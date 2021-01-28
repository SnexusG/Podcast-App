package com.example.podcastapp.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.podcastapp.R
import com.example.podcastapp.Service.ItunesService
import com.example.podcastapp.adapter.PodcastListAdapter
import com.example.podcastapp.repository.ItunesRepo
import com.example.podcastapp.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.activity_podcast.*


class PodcastActivity : AppCompatActivity(), PodcastListAdapter.PodcastListAdapterListener {

    val TAG = javaClass.simpleName
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var podcastListAdapter: PodcastListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_podcast)
        setupToolbar()
        setupViewModels()
        updateControls()
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
    private fun setupToolbar() {
        setSupportActionBar(toolbar)
    }


    private fun performSearch(term: String){
        showProgressBar()
        searchViewModel.searchPodcast(term) { results ->
            Log.d(TAG, "RESULTS : $results")
            hideProgressBar()
            toolbar.title = getString(R.string.search_results)
            podcastListAdapter.setSearchData(results)
        }
    }

    private fun setupViewModels(){
        val service = ItunesService.instance
        searchViewModel = ViewModelProviders.of(this).get(
                SearchViewModel::class.java
        )
        searchViewModel.itunesRepo = ItunesRepo(service)
    }

    private fun updateControls(){
        PodcastRecyclerView.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        PodcastRecyclerView.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(PodcastRecyclerView.context, layoutManager.orientation)
        PodcastRecyclerView.addItemDecoration(dividerItemDecoration)

        podcastListAdapter = PodcastListAdapter(null, this ,this)
        PodcastRecyclerView.adapter = podcastListAdapter
    }

    private fun showProgressBar(){
        progress_bar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        progress_bar.visibility = View.INVISIBLE
    }

    override fun onShowDetails(podcastSummaryViewData: SearchViewModel.PodcastSummaryViewData) {
        Toast.makeText(this, "Tapped", Toast.LENGTH_SHORT).show()
    }
}