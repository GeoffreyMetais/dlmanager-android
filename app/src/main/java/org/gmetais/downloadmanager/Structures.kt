package org.gmetais.downloadmanager

data class File(
        val Path: String,
        val IsDir: Boolean,
        val Size: Long)
data class Directory(val Path: String,
                     val Files: List<File>)
data class SharedFile(val Name: String,
                      val Path: String,
                      val Link: String)
data class User(val Id: String,
                val Name: String)
data class RequestBody(val Path: String,
                       val User: String)