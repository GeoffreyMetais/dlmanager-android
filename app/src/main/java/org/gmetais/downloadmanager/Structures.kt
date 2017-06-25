package org.gmetais.downloadmanager

data class File(
        val path: String,
        val isDirectory: Boolean,
        val size: Long)
data class Directory(val path: String,
                     val files: List<File>)
data class SharedFile(val path: String,
                      val name: String = path.getNameFromPath(),
                      val link: String = "")
data class User(val id: String,
                val name: String)
data class RequestBody(val path: String,
                       val user: String)

