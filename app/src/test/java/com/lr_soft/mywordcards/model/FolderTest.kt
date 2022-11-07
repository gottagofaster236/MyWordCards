package com.lr_soft.mywordcards.model

import com.lr_soft.mywordcards.test_util.FolderTestUtil.anotherChild
import com.lr_soft.mywordcards.test_util.FolderTestUtil.child
import com.lr_soft.mywordcards.test_util.FolderTestUtil.englishGermanWordPairs
import com.lr_soft.mywordcards.test_util.FolderTestUtil.grandchild
import com.lr_soft.mywordcards.test_util.FolderTestUtil.root
import com.lr_soft.mywordcards.test_util.FolderTestUtil.updatedChild
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

internal class FolderTest {

    @Test
    fun testNormalCreation() {
        // Should not throw.
        Folder(
            name = "folder",
            wordPairs = englishGermanWordPairs,
            subfolders = listOf(child),
            lastGameResults = null,
            incorrectlyGuessedWordPairs = englishGermanWordPairs
        )
    }

    @Test
    fun testBlankName() {
        assertThrows(InvalidFolderNameException::class.java) {
            Folder(
                name = "",
                wordPairs = englishGermanWordPairs,
                subfolders = listOf(child),
                lastGameResults = null,
                incorrectlyGuessedWordPairs = englishGermanWordPairs
            )
        }
    }

    @Test
    fun testDuplicateSubfolders() {
        assertThrows(DuplicateSubfoldersException::class.java) {
            Folder(
                name = "folder",
                wordPairs = englishGermanWordPairs,
                subfolders = listOf(child, child.copy(lastGameResults = GameResults(0, 0))),
                lastGameResults = null,
                incorrectlyGuessedWordPairs = englishGermanWordPairs
            )
        }
    }

    @Test
    fun testDuplicateWordPairs() {
        assertThrows(DuplicateWordPairsException::class.java) {
            Folder(
                name = "folder",
                wordPairs = englishGermanWordPairs + englishGermanWordPairs,
                subfolders = listOf(child),
                lastGameResults = null,
                incorrectlyGuessedWordPairs = englishGermanWordPairs
            )
        }
    }

    @Test
    fun testDuplicateIncorrectlyGuessedWordPairs() {
        assertThrows(DuplicateWordPairsException::class.java) {
            Folder(
                name = "folder",
                wordPairs = englishGermanWordPairs,
                subfolders = listOf(child),
                lastGameResults = null,
                incorrectlyGuessedWordPairs = englishGermanWordPairs + englishGermanWordPairs
            )
        }
    }

    @Test
    fun testCreateSubfolder() {
        assertEquals(
            root.copy(subfolders = listOf(child, anotherChild, updatedChild)),
            root.createSubfolder(updatedChild)
        )
        assertThrows(DuplicateSubfoldersException::class.java) {
            root.createSubfolder(child)
        }
        assertThrows(InvalidFolderNameException::class.java) {
            root.createSubfolder(Folder(name = "  "))
        }
    }

    @Test
    fun testDeleteSubfolder() {
        assertThrows(IllegalArgumentException::class.java) {
            root.deleteSubfolder(grandchild)
        }
        val rootWithoutChild = root.deleteSubfolder(child)
        assertEquals(root.copy(subfolders = listOf(anotherChild)), rootWithoutChild)
    }

    @Test
    fun testMoveSubfolder() {
        // The root folder has two subfolders: "child" and "anotherChild".
        val rootPathWithSubfoldersSwapped = root.copy(subfolders = listOf(anotherChild, child))
        assertEquals(root, root.moveSubfolder(child, Folder.MoveDirection.UP))
        assertEquals(
            rootPathWithSubfoldersSwapped,
            root.moveSubfolder(child, Folder.MoveDirection.DOWN)
        )
        assertEquals(root, root.moveSubfolder(anotherChild, Folder.MoveDirection.DOWN))
        assertEquals(
            rootPathWithSubfoldersSwapped,
            root.moveSubfolder(child, Folder.MoveDirection.DOWN)
        )

        // Test a folder with three subfolders.
        val threeChildren = root.copy(subfolders = listOf(child, anotherChild, updatedChild))
        val threeChildrenAfterMove =
            root.copy(subfolders = listOf(child, updatedChild, anotherChild))
        assertEquals(
            threeChildrenAfterMove,
            threeChildren.moveSubfolder(updatedChild, Folder.MoveDirection.UP)
        )

        assertThrows(IllegalArgumentException::class.java) {
            root.moveSubfolder(root, Folder.MoveDirection.UP)
        }
    }
}
