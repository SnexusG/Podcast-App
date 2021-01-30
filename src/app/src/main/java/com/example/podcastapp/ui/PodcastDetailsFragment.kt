package com.example.podcastapp.ui

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.podcastapp.R
import com.example.podcastapp.viewmodel.PodcastViewModel
import kotlinx.android.synthetic.main.fragment_podcast_details.*

class PodcastDetailsFragment : Fragment() {

    private lateinit var podcastViewModel: PodcastViewModel
    private lateinit var feedImageView : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        feedImageView = view.findViewById(R.id.feedImageView)
        setupViewModel()
        updateControls()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_podcast_details, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_details, menu)
    }

    private fun setupViewModel(){
        activity?.let{
            podcastViewModel = ViewModelProviders.of(activity!!)
                    .get(PodcastViewModel::class.java)
        }
    }

    private fun updateControls(){
        val viewData = podcastViewModel.activePodcastViewData ?: return
        feedTitleTextView?.text = viewData.feedTitle
        feedDescTextView?.text = viewData.feedDesc
        activity?.let{activity ->
            Glide.with(activity).load(viewData.imageUrl)
                    .into(feedImageView)
        }
    }

    companion object{
        fun newInstance(): PodcastDetailsFragment{
            return PodcastDetailsFragment()
        }
    }

}