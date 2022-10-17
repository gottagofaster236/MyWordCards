package com.lr_soft.mywordcards.data

import org.junit.Test
import org.junit.Assert.*

internal class FolderPathTest {

    private val grandchild = Folder(name = "3", listOf(), listOf(), null, setOf())
    private val child = Folder(name = "2", listOf(), listOf(grandchild), null, setOf())
    private val anotherChild = Folder(name = "2.1", listOf(), listOf(), null, setOf())
    private val root = Folder(name = "1", listOf(), listOf(child, anotherChild), null, setOf())
    private val rootPath = FolderPath(listOf(root))
    private val childPath = FolderPath(listOf(root, child))
    private val grandchildPath = FolderPath(listOf(root, child, grandchild))

    private val updatedChild = Folder(name = "2 new", listOf(), listOf(), null, setOf())
    private val updatedRoot = Folder(name = "1", listOf(), listOf(updatedChild, anotherChild), null, setOf())
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
        assertThrows(IllegalStateException::class.java) { rootPath.goUpOneFolder() }
    }

    @Test
    fun testGoTo() {
        assertPathEquals(childPath, rootPath.goToSubfolder(child))
        assertPathEquals(grandchildPath, childPath.goToSubfolder(grandchild))
        assertThrows(IllegalStateException::class.java) { rootPath.goToSubfolder(grandchild) }
        assertThrows(IllegalStateException::class.java) { rootPath.goToSubfolder(root) }
    }

    @Test
    fun testUpdateCurrentFolder() {
        assertPathEquals(updatedChildPath, childPath.updateCurrentFolder(updatedChild))
    }

    private fun assertFolderEquals(expected: Folder, actual: Folder) {
        assertEquals(expected, actual)
        assertEquals(expected.subfolders, actual.subfolders)
    }

    private fun assertPathEquals(expected: FolderPath, actual: FolderPath) {
        assertEquals(expected.folders.size, actual.folders.size)
        for (i in expected.folders.indices) {
            assertFolderEquals(expected.folders[i], actual.folders[i])
        }
    }
}