package org.gmetais.downloadmanager.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.gmetais.downloadmanager.getNameFromPath

data class File(
        val path: String,
        val isDirectory: Boolean,
        val size: Long)
data class Directory(val path: String,
                     val files: List<File>)
@Entity(tableName = "shares")
data class SharedFile(@PrimaryKey val path: String,
                      val name: String = path.getNameFromPath(),
                      val link: String = "")
data class User(val id: String,
                val name: String)
data class RequestBody(val path: String,
                       val user: String)

sealed class Result
data class Success<out T>(val content: T) : Result()
data class Error(val code: Int, val message: String) : Result()

