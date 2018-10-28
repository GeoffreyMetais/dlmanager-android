package org.gmetais.tools

open class SingletonHolder<out T, in A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator
    @Volatile private var instance: T? = null

    fun getInstance(arg: A) = instance ?: synchronized(this) {
        val i2 = instance
        if (i2 != null) i2
        else {
            val created = creator!!(arg)
            instance = created
            creator = null
            created
        }
    }
}
