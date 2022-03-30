package com.example.compose.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.compose.local.model.*

@Database(
    entities = [Song::class, Artist::class, Album::class, Folder::class, PlayList::class, PlaylistItem::class],
    version = 1
)
abstract class DataBase : RoomDatabase() {

    companion object {

        @Volatile
        private var INSTANCE: DataBase? = null

        fun getDataBase(context: Context): DataBase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null)
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DataBase::class.java,
                        "song_database"
                    ).allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }
        }
    }

    abstract val modificationDao: ModificationDao
    abstract val songDao: SongDao
    abstract val folderDao: FolderDao
    abstract val artistDao: ArtistDao
    abstract val albumDao: AlbumDao
    abstract val playlistDao: PlaylistDao
}