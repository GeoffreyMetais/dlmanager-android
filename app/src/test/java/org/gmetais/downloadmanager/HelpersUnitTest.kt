package org.gmetais.downloadmanager

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class HelpersUnitTest {
    @Test
    fun getNameFromPath() {
        assertEquals("filename.ext", "/folder/filename.ext".getNameFromPath())
        assertEquals("subfolder", "/folder/subfolder/".getNameFromPath())
        assertEquals("", "/".getNameFromPath())
        assertEquals("folder", "/folder".getNameFromPath())
        assertEquals("folder", "/folder/".getNameFromPath())
    }
}
