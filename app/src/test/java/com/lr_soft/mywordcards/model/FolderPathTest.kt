package com.lr_soft.mywordcards.model

import com.lr_soft.mywordcards.test_util.FolderTestUtil.assertFolderEquals
import com.lr_soft.mywordcards.test_util.FolderTestUtil.assertPathEquals
import com.lr_soft.mywordcards.test_util.FolderTestUtil.child
import com.lr_soft.mywordcards.test_util.FolderTestUtil.childPath
import com.lr_soft.mywordcards.test_util.FolderTestUtil.grandchild
import com.lr_soft.mywordcards.test_util.FolderTestUtil.grandchildPath
import com.lr_soft.mywordcards.test_util.FolderTestUtil.root
import com.lr_soft.mywordcards.test_util.FolderTestUtil.rootPath
import com.lr_soft.mywordcards.test_util.FolderTestUtil.updatedChild
import com.lr_soft.mywordcards.test_util.FolderTestUtil.updatedChildPath
import org.junit.Assert
import org.junit.Test

internal class FolderPathTest {
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
}
