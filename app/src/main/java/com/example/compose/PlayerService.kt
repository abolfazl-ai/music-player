package com.example.compose


import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.example.compose.local.model.PlayList
import com.example.compose.local.model.PlaylistItem
import com.example.compose.local.model.Song
import com.example.compose.local.room.DataBase
import com.example.compose.local.room.PlaylistDao
import com.example.compose.utils.util_classes.PlaybackAction
import com.example.compose.utils.util_classes.PlaybackAction.*
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ShuffleOrder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PlayerService : LifecycleService() {

    private lateinit var dao: PlaylistDao
    private var playlistId: Int = 1

    private val TAG = "Abolfazl"

    private val iBinder: IBinder by lazy { PlayerBinder() }

    private lateinit var player: SimpleExoPlayer

    private var onPlaylistChanged: (List<Song>) -> Unit = {}
    private var playerListener: Player.Listener? = null

    var isShuffleModeOn = false

    private var playlist = ArrayList<Pair<Song, Int>>()
        set(value) {
            savePlaylist(value)
            field = value
        }

    private var unShuffledPlaylist: ArrayList<Pair<Song, Int>> = ArrayList()

    private var shuffleOrder: ShuffleOrder? = null

    override fun onCreate() {
        super.onCreate()
        player = SimpleExoPlayer.Builder(applicationContext).build()

        lifecycleScope.launch {
            dao = DataBase.getDataBase(applicationContext).playlistDao
            with(dao.getCurrentPlaylist()) {
                if (this == null) {
                    dao.insertPlaylist(PlayList(playlistId, "current", isCurrent = true))
                } else {
                    playlistId = playlist.id
                    setPlaylist(items.map { it.song }, 0)
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return iBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        removeListeners()
        return super.onUnbind(intent)
    }

    private fun removeListeners() {
        onPlaylistChanged = {}
        playerListener?.let { player.removeListener(it) }
        playerListener = null
    }

    fun setPlaylist(songs: List<Song>, index: Int) {
        CoroutineScope(Dispatchers.IO).launch {

            unShuffledPlaylist = ArrayList(songs.mapIndexed { index, song -> Pair(song, index) })
            playlist = ArrayList(unShuffledPlaylist)

            val mediaItems = songs.mapIndexed { i: Int, song: Song ->
                MediaItem.Builder().setUri(song.path).setMediaId(i.toString()).build()
            }

            withContext(Dispatchers.Main) {
                player.clearMediaItems()
                player.setMediaItems(mediaItems)
                player.seekTo(index, C.TIME_UNSET)
                player.prepare()
            }

        }
    }

    fun shuffle(value: Boolean = !isShuffleModeOn) {
        CoroutineScope(Dispatchers.Default).launch {
            if (value) {
                playlist.shuffle()
                shuffleOrder = ShuffleOrder(playlist.map { it.second })
            } else {
                playlist.clear()
                playlist.addAll(unShuffledPlaylist)
            }

            isShuffleModeOn = value
            playlist.forEach {
                Log.d(TAG, "shuffle: ${it.first.title}")
            }
            Log.e(TAG, "shuffle: End")
            savePlaylist(playlist)

            withContext(Dispatchers.Main) {
                if (shuffleOrder != null) player.setShuffleOrder(shuffleOrder!!)
                player.shuffleModeEnabled = value
            }
        }
    }

    private fun savePlaylist(p: List<Pair<Song, Int>>) {
        lifecycleScope.launch {
            onPlaylistChanged(p.map { it.first })
//            dao.clearCurrentPlaylist(playlistId)
            dao.insertPlaylistItems(p.map { PlaylistItem(it.second, playlistId, it.first) })
        }
    }

    override fun onDestroy() {
        player.release()
        super.onDestroy()
    }

    inner class PlayerBinder : Binder() {

        fun getService(): PlayerService = this@PlayerService

        val playbackController: (PlaybackAction) -> Unit = {
            with(player) {
                when (it) {
                    PLAY -> {
                        if (playWhenReady) pause() else play()
                    }
                    PAUSE -> pause()
                    NEXT -> seekToNextWindow()
                    PREVIOUS -> seekToPreviousWindow()
                    STOP -> stop()
                    SHUFFLE -> shuffle(!isShuffleModeOn)
                    REPEAT -> {
                    }
                }
            }
        }

        val seekTo: (index: Int, progress: Long) -> Unit =
            { index, progress -> player.seekTo(index, progress) }

        fun registerListener(listener: Player.Listener) {
            player.addListener(listener)
            playerListener = listener
        }

        fun registerOnPlaylistChangeListener(listener: (List<Song>) -> Unit) {
            onPlaylistChanged = listener
        }

    }

}

class ShuffleOrder(private val shuffled: List<Int>) : ShuffleOrder {

    override fun getLength(): Int = shuffled.size

    override fun getNextIndex(index: Int): Int {
        shuffled.indexOf(index).let {
            return if (it < shuffled.lastIndex) shuffled[it + 1] else shuffled.last()
        }
    }

    override fun getPreviousIndex(index: Int): Int {
        shuffled.indexOf(index).let {
            return if (it > 0) shuffled[it - 1] else shuffled.first()
        }
    }

    override fun getLastIndex(): Int = shuffled.last()

    override fun getFirstIndex(): Int = shuffled.first()

    override fun cloneAndInsert(insertionIndex: Int, insertionCount: Int): ShuffleOrder = this

    override fun cloneAndRemove(indexFrom: Int, indexToExclusive: Int): ShuffleOrder = this

    override fun cloneAndClear(): ShuffleOrder = this

}