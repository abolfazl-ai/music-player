package com.example.compose.viewmodel

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.MutableLiveData
import com.example.compose.PlayerService
import com.example.compose.local.model.Song
import com.example.compose.utils.util_classes.PlaybackAction
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class ServiceController @Inject constructor() {

    val binder: MutableLiveData<PlayerService.PlayerBinder?> = MutableLiveData()

    val serviceConnection: ServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val mBinder: PlayerService.PlayerBinder = service as PlayerService.PlayerBinder
            binder.postValue(mBinder)
            mBinder.registerOnPlaylistChangeListener(playlistListener)
            playbackController = mBinder.playbackController
            seekTo = mBinder.seekTo
            mBinder.registerListener(playerLister)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            binder.postValue(null)
            playbackController = {}
            seekTo = { _, _ -> }
        }
    }

    val playerLister = object : Player.Listener {
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            if (mediaItem != null) currentIndex.value = mediaItem.mediaId.toInt()
        }
    }

    private val playlistListener: (List<Song>) -> Unit = {
        playlist.value = it
    }

    var playlist: MutableStateFlow<List<Song>> = MutableStateFlow(emptyList())

    val currentIndex = MutableStateFlow(0)

    var playbackController: (PlaybackAction) -> Unit = {}; private set

    var seekTo: (index: Int, progress: Long) -> Unit = { _, _ -> }; private set

}