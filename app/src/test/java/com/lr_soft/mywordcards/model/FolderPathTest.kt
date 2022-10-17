package com.lr_soft.mywordcards.model

import org.junit.Assert
import org.junit.Test

internal class FolderPathTest {

    private val grandchild =
        Folder(name = "3", emptyList(), emptyList(), null, emptySet())
    private val child =
        Folder(name = "2", emptyList(), listOf(grandchild), null, emptySet())
    private val anotherChild =
        Folder(name = "2.1", emptyList(), emptyList(), null, emptySet())
    private val root =
        Folder(name = "1", emptyList(), listOf(child, anotherChild), null, emptySet())
    private val rootPath = FolderPath(listOf(root))
    private val childPath = FolderPath(listOf(root, child))
    private val grandchildPath = FolderPath(listOf(root, child, grandchild))

    private val updatedChild =
        Folder(name = "2 new", emptyList(), emptyList(), null, emptySet())
    private val updatedRoot =
        Folder(name = "1", emptyList(), listOf(updatedChild, anotherChild), null, emptySet())
    private val updatedChildPath = FolderPath(listOf(updatedRoot, updatedChild))

    @Test
    fun testGetCurrentFolder() {
        assertFolderEquals(grandchild, grandchildPath.currentFolder)
        assertFolderEquals(child, childPath.currentFolder)
        assertFolderEquals(root, rootPath.currentFolder)
    }

    @Test
    fun testGoUpOneFolder() {
        assertPathEquals(childPath, grandchildPath.goUpOneFolder())
        assertPathEquals(rootPath, childPath.goUpOneFolder())
        Assert.assertThrows(IllegalStateException::class.java) { rootPath.goUpOneFolder() }
    }

    @Test
    fun testGoTo() {
        assertPathEquals(childPath, rootPath.goToSubfolder(child))
        assertPathEquals(grandchildPath, childPath.goToSubfolder(grandchild))
        Assert.assertThrows(IllegalStateException::class.java) { rootPath.goToSubfolder(grandchild) }
        Assert.assertThrows(IllegalStateException::class.java) { rootPath.goToSubfolder(root) }
    }

    @Test
    fun testUpdateCurrentFolder() {
        assertPathEquals(updatedChildPath, childPath.updateCurrentFolder(updatedChild))
    }

    private fun assertFolderEquals(expected: Folder, actual: Folder) {
        Assert.assertEquals(expected, actual)
        Assert.assertEquals(expected.subfolders, actual.subfolders)
    }

    private fun assertPathEquals(expected: FolderPath, actual: FolderPath) {
        Assert.assertEquals(expected.folders.size, actual.folders.size)
        for (i in expected.folders.indices) {
            assertFolderEquals(expected.folders[i], actual.folders[i])
        }
    }
}
