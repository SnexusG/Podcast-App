package com.example.podcastapp.ui

import android.content.ComponentName
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.podcastapp.R
import com.example.podcastapp.Service.PodplayMediaService
import com.example.podcastapp.util.HtmlUtils
import com.example.podcastapp.viewmodel.PodcastViewModel
import kotlinx.android.synthetic.main.fragment_episode_player.*

class EpisodePlayerFragment : Fragment() {

    private lateinit var podcastViewModel: PodcastViewModel
    private lateinit var mediaBrowser: MediaBrowserCompat
    private var mediaControllerCallback: MediaControllerCallback? = null
    private lateinit var playToggleButton : Button

    inner class MediaControllerCallback : MediaControllerCompat.Callback(){

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
            print("metadata changed to ${metadata?.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI)}")
        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)
            print("state changed to $state")
            val state = state ?: return
            handleStateChange(state.getState())
        }
    }

    inner class MediaBrowserCallBacks: MediaBrowserCompat.ConnectionCallback() {

        override fun onConnected() {
            super.onConnected()
            registerMediaController(mediaBrowser.sessionToken)
            println("onConnected")
        }
        override fun onConnectionSuspended() {
            super.onConnectionSuspended()
            println("onConnectionSuspended")
        }

        override fun onConnectionFailed() {
            super.onConnectionFailed()
            println("onConnectionFailed")
        }
    }


    private fun togglePlayPause(){
        val fragmentActivity = activity as FragmentActivity
        val controller =
                MediaControllerCompat.getMediaController(fragmentActivity)
        if (controller.playbackState != null) {
            if (controller.playbackState.state ==PlaybackStateCompat.STATE_PLAYING) {
                controller.transportControls.pause()
            } else {
                podcastViewModel.activeEpisodeViewData?.let { startPlaying(it) }
            }
        } else {
            podcastViewModel.activeEpisodeViewData?.let { startPlaying(it) }
        }
    }

    private fun setupControls() {
        playToggleButton.setOnClickListener {
            Toast.makeText(activity, "Clicked", Toast.LENGTH_SHORT).show()
            togglePlayPause()
        }
    }

    private fun handleStateChange(state: Int) {
        val isPlaying = state == PlaybackStateCompat.STATE_PLAYING
        playToggleButton.isActivated = isPlaying
    }

    private fun initMediaBrowser() {
        val fragmentActivity = activity as FragmentActivity
        mediaBrowser = MediaBrowserCompat(
                fragmentActivity,
                ComponentName(fragmentActivity, PodplayMediaService::class.java),
                MediaBrowserCallBacks(), null
        )
    }

    private fun setupViewModel() {
        val fragmentActivity = activity as FragmentActivity
        podcastViewModel = ViewModelProviders.of(fragmentActivity).get(PodcastViewModel::class.java)
    }

    private fun updateControls(){
        episodeTitleTextView.text = podcastViewModel.activeEpisodeViewData?.title
        val htmlDesc = podcastViewModel.activeEpisodeViewData?.description ?: ""
        val descSpan = HtmlUtils.htmlToSpannable(htmlDesc)
        episodeDescTextView.text = descSpan
        episodeDescTextView.movementMethod = ScrollingMovementMethod()
        val fragmentActivity = activity as FragmentActivity
        Glide.with(fragmentActivity).load(podcastViewModel.activePodcastViewData?.imageUrl).into(episodeImageView)
    }

    companion object {
        fun newInstance(): EpisodePlayerFragment {
            return EpisodePlayerFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setupViewModel()
        initMediaBrowser()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(
            R.layout.fragment_episode_player,
            container, false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playToggleButton = view.findViewById(R.id.playToggleButton)
        setupControls()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateControls()
    }

    override fun onStart() {
        super.onStart()
        if(mediaBrowser.isConnected){
            val fragmentActivity = activity as FragmentActivity
            if(MediaControllerCompat.getMediaController(fragmentActivity) == null){
                registerMediaController(mediaBrowser.sessionToken)
            }
        }else{
            mediaBrowser.connect()
        }
    }

    override fun onStop() {
        super.onStop()
        val fragmentActivity = activity as FragmentActivity
        if (MediaControllerCompat.getMediaController(fragmentActivity) != null)
        {
            mediaControllerCallback?.let {
                MediaControllerCompat.getMediaController(fragmentActivity)
                        .unregisterCallback(it)
            }
        }
    }

    private fun startPlaying(episodeViewData : PodcastViewModel.EpisodeViewData){
        val fragmentActivity = activity as FragmentActivity
        val controller = MediaControllerCompat.getMediaController(fragmentActivity)
        val viewData = podcastViewModel.activePodcastViewData ?: return
        val bundle = Bundle()
        bundle.putString(MediaMetadataCompat.METADATA_KEY_TITLE,
                episodeViewData.title)
        bundle.putString(MediaMetadataCompat.METADATA_KEY_ARTIST,
                viewData.feedTitle)
        bundle.putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI,
                viewData.imageUrl)
        controller.transportControls.playFromUri(Uri.parse(episodeViewData.mediaUrl), bundle)
    }

    private fun registerMediaController(token : MediaSessionCompat.Token) {
        val fragmentActivity = activity as FragmentActivity
        val mediaController = MediaControllerCompat(fragmentActivity, token)
        MediaControllerCompat.setMediaController(fragmentActivity, mediaController)
        mediaControllerCallback = MediaControllerCallback()
        mediaController.registerCallback(mediaControllerCallback!!)
    }
}