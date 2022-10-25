package com.lr_soft.mywordcards.data

import android.content.Context
import com.lr_soft.mywordcards.model.*
import com.lr_soft.mywordcards.test_util.FolderTestUtil.englishFrenchWordPairs
import com.lr_soft.mywordcards.test_util.FolderTestUtil.englishGermanWordPairs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*

@RunWith(MockitoJUnitRunner::class)
internal class RootFolderRepositoryTest {

    @get:Rule
    val folder = TemporaryFolder()

    private val context = mock<Context> {
        on { filesDir } doAnswer { folder.root }
    }

    private lateinit var repository: RootFolderRepository

    private val child1 = Folder(
        name = "en to de",
        wordPairs = englishGermanWordPairs,
        subfolders = emptyList(),
        lastGameResults = GameResults(1, 1),
        incorrectlyGuessedWordPairs = listOf(englishGermanWordPairs[0])
    )

    private val child2 = Folder(
        name = "en to fr",
        wordPairs = englishFrenchWordPairs,
        subfolders = emptyList(),
        lastGameResults = GameResults(0, 1),
        incorrectlyGuessedWordPairs = englishFrenchWordPairs
    )

    private val rootFolder = Folder(
        name = "root",
        wordPairs = emptyList(),
        subfolders = listOf(child1, child2),
        lastGameResults = GameResults(1, 2),
        incorrectlyGuessedWordPairs =
            child1.incorrectlyGuessedWordPairs + child2.incorrectlyGuessedWordPairs
    )

    @Test
    fun testSaveLoad() = runBlocking {
        createRepository()
        repository.saveRootFolder(rootFolder)
        val loaded = repository.loadRootFolder()
        assertEquals(rootFolder, loaded)
    }

    @Test
    fun testLoadNothing() = runBlocking {
        createRepository()
        whenever(context.getString(any())) doReturn "root"
        assertEquals(Folder.emptyRootFolder(context), repository.loadRootFolder())
    }

    private fun CoroutineScope.createRepository() {
        repository = RootFolderRepository(context, this)
    }
}
