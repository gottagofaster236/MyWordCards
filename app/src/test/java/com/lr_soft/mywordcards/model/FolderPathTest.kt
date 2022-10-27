package com.lr_soft.mywordcards.model

import com.lr_soft.mywordcards.test_util.FolderTestUtil.anotherChild
import com.lr_soft.mywordcards.test_util.FolderTestUtil.child
import com.lr_soft.mywordcards.test_util.FolderTestUtil.childPath
import com.lr_soft.mywordcards.test_util.FolderTestUtil.englishFrenchWordPairs
import com.lr_soft.mywordcards.test_util.FolderTestUtil.grandchild
import com.lr_soft.mywordcards.test_util.FolderTestUtil.grandchildPath
import com.lr_soft.mywordcards.test_util.FolderTestUtil.root
import com.lr_soft.mywordcards.test_util.FolderTestUtil.rootPath
import com.lr_soft.mywordcards.test_util.FolderTestUtil.updatedChild
import com.lr_soft.mywordcards.test_util.FolderTestUtil.updatedChildPath
import com.lr_soft.mywordcards.test_util.FolderTestUtil.updatedRootPath
import org.junit.Assert.*
import org.junit.Test

internal class FolderPathTest {
    
    @Test
    fun testEquals() {
        val childWithWords = child.copy(wordPairs = englishFrenchWordPairs)
        val rootWithWords = root.copy(subfolders = listOf(childWithWords, anotherChild))
        assertNotEquals(FolderPath(root), FolderPath(rootWithWords))
        assertNotEquals(FolderPath(listOf(root, child)),
            FolderPath(listOf(rootWithWords, rootWithWords)))

        assertEquals(FolderPath(root), FolderPath(root.copy()))
        assertEquals(FolderPath(root).hashCode(), FolderPath(root.copy()).hashCode())
    }

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
        assertThrows(IllegalArgumentException::class.java) { rootPath.goUpOneFolder() }
    }

    @Test
    fun testGoTo() {
        assertEquals(childPath, rootPath.goToSubfolder(child))
        assertEquals(grandchildPath, childPath.goToSubfolder(grandchild))
        assertThrows(IllegalArgumentException::class.java) {
            rootPath.goToSubfolder(grandchild)
        }
        assertThrows(IllegalArgumentException::class.java) {
            rootPath.goToSubfolder(root)
        }
    }

    @Test
    fun testUpdateCurrentFolder() {
        assertEquals(updatedChildPath, childPath.updateCurrentFolder(updatedChild))
        assertEquals(updatedChildPath, childPath.updateCurrentFolder {
            assertEquals(this, child)
            updatedChild
        })
    }

    @Test
    fun testUpdateSubfolder() {
        assertEquals(updatedRootPath, rootPath.updateSubfolder(child, updatedChild))
    }
}
