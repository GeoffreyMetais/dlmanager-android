package org.gmetais.downloadmanager.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import org.gmetais.downloadmanager.Application

@Database(entities = arrayOf(SharedFile::class), version = 1, exportSchema = false)
abstract class SharesDatabase : RoomDatabase() {
    companion object {
        val db = Room.databaseBuilder(Application.context, SharesDatabase::class.java, "shares").build()
    }
    abstract fun sharesDao(): SharesDao
}