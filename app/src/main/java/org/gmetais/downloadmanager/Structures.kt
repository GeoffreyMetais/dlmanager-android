package org.gmetais.downloadmanager

data class File(
        val path: String,
        val isDir: Boolean,
        val size: Long)
data class Directory(val path: String,
                     val files: List<File>)
data class SharedFile(val name: String,
                      val path: String,
                      val link: String)
data class User(val id: String,
                val name: String)
data class RequestBody(val path: String,
                       val user: String)

fun  String.getNameFromPath(): String {
    var trailing = this.endsWith('/')
    var index = if (!trailing) this.lastIndexOf('/') else this.substring(0, this.length-2).lastIndexOf('/')
    return this.substring(index+1, if (trailing) this.length-1 else this.length)
}
