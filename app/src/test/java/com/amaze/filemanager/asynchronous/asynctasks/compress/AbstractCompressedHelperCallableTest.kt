/*
 * Copyright (C) 2014-2021 Arpit Khurana <arpitkh96@gmail.com>, Vishal Nehra <vishalmeham2@gmail.com>,
 * Emmanuel Messulam<emmanuelbendavid@gmail.com>, Raymond Lai <airwave209gt at gmail.com> and Contributors.
 *
 * This file is part of Amaze File Manager.
 *
 * Amaze File Manager is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.amaze.filemanager.asynchronous.asynctasks.compress

import android.os.Build.VERSION_CODES.*
import android.os.Environment
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.amaze.filemanager.adapters.data.CompressedObjectParcelable
import com.amaze.filemanager.asynchronous.asynctasks.AsyncTaskResult
import com.amaze.filemanager.shadows.ShadowMultiDex
import com.amaze.filemanager.utils.OnAsyncTaskFinished
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowEnvironment
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.time.ZoneId
import java.time.ZonedDateTime

@RunWith(AndroidJUnit4::class)
@Config(shadows = [ShadowMultiDex::class], sdk = [JELLY_BEAN, KITKAT, P])
@Suppress("TooManyFunctions", "StringLiteralDuplication")
abstract class AbstractCompressedHelperCallableTest {

    private val EXPECTED_TIMESTAMP = ZonedDateTime.of(
        2018,
        5,
        29,
        10,
        38,
        0,
        0,
        ZoneId.of("UTC")
    ).toInstant().toEpochMilli()

    /**
     * Test setup.
     */
    @Before
    fun setUp() {
        ShadowEnvironment.setExternalStorageState(Environment.MEDIA_MOUNTED)
        copyArchivesToStorage()
    }

    /**
     * Test browse archive top level.
     */
    @Test
    open fun testRoot() {
        val task = createTask("")
        val result = task.call()
        Assert.assertEquals(1, result.size.toLong())
        Assert.assertEquals("test-archive", result[0].name)
        Assert.assertEquals(EXPECTED_TIMESTAMP, result[0].date)
    }

    /**
     * Test browse archive sub levels.
     */
    @Test
    open fun testSublevels() {
        var task = createTask("test-archive")
        var result = task.call()
        Assert.assertEquals(5, result.size.toLong())
        Assert.assertEquals("1", result[0].name)
        Assert.assertEquals(EXPECTED_TIMESTAMP, result[0].date)
        Assert.assertEquals("2", result[1].name)
        Assert.assertEquals(EXPECTED_TIMESTAMP, result[1].date)
        Assert.assertEquals("3", result[2].name)
        Assert.assertEquals(EXPECTED_TIMESTAMP, result[2].date)
        Assert.assertEquals("4", result[3].name)
        Assert.assertEquals(EXPECTED_TIMESTAMP, result[3].date)
        Assert.assertEquals("a", result[4].name)
        Assert.assertEquals(EXPECTED_TIMESTAMP, result[4].date)
        task = createTask("test-archive/1")
        result = task.call()
        Assert.assertEquals(1, result.size.toLong())
        Assert.assertEquals("8", result[0].name)
        Assert.assertEquals(EXPECTED_TIMESTAMP, result[0].date)
        task = createTask("test-archive/2")
        result = task.call()
        Assert.assertEquals(1, result.size.toLong())
        Assert.assertEquals("7", result[0].name)
        Assert.assertEquals(EXPECTED_TIMESTAMP, result[0].date)
        task = createTask("test-archive/3")
        result = task.call()
        Assert.assertEquals(1, result.size.toLong())
        Assert.assertEquals("6", result[0].name)
        Assert.assertEquals(EXPECTED_TIMESTAMP, result[0].date)
        task = createTask("test-archive/4")
        result = task.call()
        Assert.assertEquals(1, result.size.toLong())
        Assert.assertEquals("5", result[0].name)
        Assert.assertEquals(EXPECTED_TIMESTAMP, result[0].date)
        task = createTask("test-archive/a")
        result = task.call()
        Assert.assertEquals(1, result.size.toLong())
        Assert.assertEquals("b", result[0].name)
        Assert.assertEquals(EXPECTED_TIMESTAMP, result[0].date)
        task = createTask("test-archive/a/b")
        result = task.call()
        Assert.assertEquals(1, result.size.toLong())
        Assert.assertEquals("c", result[0].name)
        Assert.assertEquals(EXPECTED_TIMESTAMP, result[0].date)
        task = createTask("test-archive/a/b/c")
        result = task.call()
        Assert.assertEquals(1, result.size.toLong())
        Assert.assertEquals("d", result[0].name)
        Assert.assertEquals(EXPECTED_TIMESTAMP, result[0].date)
        task = createTask("test-archive/a/b/c/d")
        result = task.call()
        Assert.assertEquals(1, result.size.toLong())
        Assert.assertEquals("lipsum.bin", result[0].name)
        Assert.assertEquals(EXPECTED_TIMESTAMP, result[0].date)
        // assertEquals(512, result.get(0).size);
    }

    protected abstract fun createTask(relativePath: String): CompressedHelperCallable

    private fun copyArchivesToStorage() {
        File("src/test/resources").listFiles()?.forEach {
            FileInputStream(it).copyTo(
                FileOutputStream(
                    File(Environment.getExternalStorageDirectory(), it.name)
                )
            )
        }
    }
}
