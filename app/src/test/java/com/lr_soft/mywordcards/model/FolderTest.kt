package com.lr_soft.mywordcards.model

import com.lr_soft.mywordcards.test_util.FolderTestUtil.child
import com.lr_soft.mywordcards.test_util.FolderTestUtil.englishGermanWordPairs
import org.junit.Assert.*
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
}
