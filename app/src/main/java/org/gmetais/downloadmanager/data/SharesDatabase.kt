package org.gmetais.downloadmanager.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.gmetais.downloadmanager.Application

@Database(entities = [(SharedFile::class)], version = 1, exportSchema = false)
abstract class SharesDatabase : RoomDatabase() {
    abstract fun sharesDao(): SharesDao
}