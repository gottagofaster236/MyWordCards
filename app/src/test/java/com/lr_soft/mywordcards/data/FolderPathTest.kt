package com.lr_soft.mywordcards.data

import org.junit.Test
import org.junit.Assert.*

internal class FolderPathTest {

    private val grandchild = Folder(name = "3", listOf(), listOf(), null, setOf())
    private val child = Folder(name = "2", listOf(), listOf(grandchild), null, setOf())
    private val anotherChild = Folder(name = "2", listOf(), listOf(), null, setOf())
    private val root = Folder(name = "1", listOf(), listOf(child, anotherChild), null, setOf())
    private val rootPath = FolderPath(listOf(root))
    private val childPath = FolderPath(listOf(root, child))
    private val grandchildPath = FolderPath(listOf(root, child, grandchild))

    private val updatedChild = Folder(name = "2 new", listOf(), listOf(), null, setOf())
    private val updatedRoot = Folder(name = "1", listOf(), listOf(updatedChild, anotherChild), null, setOf())
    private val updatedChildPath = FolderPath(listOf(updatedRoot, updatedChild))

    @Test
    fun testGetCurrentFolder() {
        assertEquals(grandchild, grandchildPath.currentFolder)
        assertEquals(child, childPath.currentFolder)
        assertEquals(root, rootPath.currentFolder)
    }

    @Test
    fun testGoUpOneFolder() {
        assertEquals(childPath, grandchildPath.goUpOneFolder())
        assertEquals(rootPath, childPath.goUpOneFolder())
        assertThrows(IllegalStateException::class.java) { rootPath.goUpOneFolder() }
    }

    @Test
    fun testGoTo() {
        assertEquals(childPath, rootPath.goTo(child))
        assertEquals(grandchildPath, childPath.goTo(grandchild))
        assertThrows(IllegalStateException::class.java) { rootPath.goTo(grandchild) }
        assertThrows(IllegalStateException::class.java) { rootPath.goTo(root) }
    }

    @Test
    fun testUpdateCurrentFolder() {
        assertEquals(updatedChildPath, childPath.updateCurrentFolder(updatedChild))
    }
}