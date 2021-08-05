package com.amaze.filemanager.asynchronous.asynctasks.compress

import android.os.Environment
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test
import java.io.File

@Ignore("Test skipped due to problem at upstream library.")
class SevenZipHelperTaskTest2 : AbstractCompressedHelperCallableTest() {

    @Test
    override fun testRoot() {
        val task = createTask("")
        val result = task.call()
        Assert.assertEquals(result.size.toLong(), 0)
    }

    @Test
    @Ignore("Not testing this one")
    override fun testSublevels() = Unit

    override fun createTask(relativePath: String): CompressedHelperCallable = SevenZipHelperCallable(
        File(Environment.getExternalStorageDirectory(), "compress.7z").absolutePath,
        relativePath,
        false
    )
}
