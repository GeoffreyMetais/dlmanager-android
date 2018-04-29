package org.gmetais.tools

class Event<out T>(private val content: T) {

    private var handled = false

    fun getContent() : T? {
        return if (handled) null
        else {
            handled = true
            content
        }
    }

    fun peekContent() = content
}