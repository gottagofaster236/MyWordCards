package com.lr_soft.mywordcards.model

import com.lr_soft.mywordcards.test_util.FolderTestUtil.anotherChild
import com.lr_soft.mywordcards.test_util.FolderTestUtil.assertFolderEquals
import com.lr_soft.mywordcards.test_util.FolderTestUtil.child
import com.lr_soft.mywordcards.test_util.FolderTestUtil.englishGermanWordPairs
import com.lr_soft.mywordcards.test_util.FolderTestUtil.root
import com.lr_soft.mywordcards.test_util.FolderTestUtil.updatedChild
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
                subfolders = listOf(child, child),
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
        assertFolderEquals(
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
    fun testMoveSubfolder() {
        // The root folder has two subfolders: "child" and "anotherChild".
        val rootPathWithSubfoldersSwapped = root.copy(subfolders = listOf(anotherChild, child))
        assertFolderEquals(root, root.moveSubfolder(child, up = true))
        assertFolderEquals(rootPathWithSubfoldersSwapped, root.moveSubfolder(child, up = false))
        assertFolderEquals(root, root.moveSubfolder(anotherChild, up = false))
        assertFolderEquals(rootPathWithSubfoldersSwapped, root.moveSubfolder(child, up = false))
        // Test a folder with three subfolders.
        val threeChildren = root.copy(subfolders = listOf(child, anotherChild, updatedChild))
        val threeChildrenAfterMove =
            root.copy(subfolders = listOf(child, updatedChild, anotherChild))
        assertFolderEquals(threeChildrenAfterMove,
            threeChildren.moveSubfolder(updatedChild, up = true))

        assertThrows(IllegalArgumentException::class.java) {
            root.moveSubfolder(root, up = true)
        }
    }
}
