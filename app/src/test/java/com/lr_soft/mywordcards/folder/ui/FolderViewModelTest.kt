package com.lr_soft.mywordcards.folder.ui

import android.content.Context
import com.lr_soft.mywordcards.data.RootFolderRepository
import com.lr_soft.mywordcards.model.Folder
import com.lr_soft.mywordcards.test_util.FolderTestUtil
import com.lr_soft.mywordcards.ui.folder.FolderViewModel
import com.lr_soft.mywordcards.ui.folder.SubfolderEdit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
internal class FolderViewModelTest {

    private val context = mock<Context> {
        on { getString(any()) } doReturn ""
    }
    private val rootFolderRepository = mock<RootFolderRepository> {
        onBlocking { loadRootFolder() } doReturn FolderTestUtil.root
    }
    private lateinit var viewModel: FolderViewModel

    private val mainDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(mainDispatcher)
        runTest {
            createViewModel()
        }
    }

    private fun TestScope.createViewModel() {
        viewModel = FolderViewModel(rootFolderRepository, context)
        advanceUntilIdle()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testConstructor() {
        verifyBlocking(rootFolderRepository, times(1)) { loadRootFolder() }
        verifyBlocking(rootFolderRepository, never()) { saveRootFolder(any()) }
        assertNull(viewModel.uiState.userMessage)
    }

    @Test
    fun testConstructorErrors() = runTest {
        whenever(rootFolderRepository.loadRootFolder()) doAnswer { throw IOException() }
        createViewModel()
        assertNotNull(viewModel.uiState.userMessage)
    }

    @Test
    fun testStartEditingSubfolder() {
        assertNull(viewModel.uiState.subfolderEdit)

        viewModel.startEditingSubfolder(FolderTestUtil.child)
        assertEquals(
            SubfolderEdit(FolderTestUtil.child, FolderTestUtil.child.name),
            viewModel.uiState.subfolderEdit
        )

        viewModel.startEditingSubfolder(FolderTestUtil.anotherChild)
        assertEquals(
            SubfolderEdit(FolderTestUtil.anotherChild, FolderTestUtil.anotherChild.name),
            viewModel.uiState.subfolderEdit
        )

        viewModel.startEditingSubfolder(null)
        assertEquals(SubfolderEdit(null, ""), viewModel.uiState.subfolderEdit)

        assertDoesNothing {
            viewModel.startEditingSubfolder(FolderTestUtil.root)  // Not a child of current subfolder
        }
    }

    @Test
    fun testEditSubfolderName() {
        assertDoesNothing {
            viewModel.editSubfolderName("New name")
        }

        viewModel.startEditingSubfolder(null)
        assertEquals("", viewModel.uiState.subfolderEdit?.newName)
        viewModel.editSubfolderName("New name")
        assertEquals("New name", viewModel.uiState.subfolderEdit?.newName)
        viewModel.editSubfolderName("Another name")
        assertEquals("Another name", viewModel.uiState.subfolderEdit?.newName)
        viewModel.cancelSubfolderEdit()

        viewModel.startEditingSubfolder(FolderTestUtil.child)
        assertEquals(FolderTestUtil.child.name, viewModel.uiState.subfolderEdit?.newName)
    }

    @Test
    fun testDeleteSubfolder() = runTest {
        assertDoesNothing {
            viewModel.deleteSubfolder()
        }

        viewModel.startEditingSubfolder(null)
        assertDoesNothing {
            viewModel.deleteSubfolder()
        }

        viewModel.startEditingSubfolder(FolderTestUtil.root)
        assertDoesNothing {
            viewModel.deleteSubfolder()
        }

        viewModel.startEditingSubfolder(FolderTestUtil.child)
        viewModel.deleteSubfolder()
        advanceUntilIdle()
        assertRootFolderEquals(FolderTestUtil.root.deleteSubfolder(FolderTestUtil.child))
        assertNull(viewModel.uiState.subfolderEdit)
    }

    @Test
    fun testMoveSubfolder() = runTest {
        assertDoesNothing {
            viewModel.moveSubfolder(Folder.MoveDirection.DOWN)
        }

        viewModel.startEditingSubfolder(null)
        assertDoesNothing {
            viewModel.moveSubfolder(Folder.MoveDirection.DOWN)
        }

        viewModel.startEditingSubfolder(FolderTestUtil.root)
        assertDoesNothing {
            viewModel.moveSubfolder(Folder.MoveDirection.DOWN)
        }

        viewModel.startEditingSubfolder(FolderTestUtil.child)
        val oldState = viewModel.uiState
        viewModel.moveSubfolder(Folder.MoveDirection.DOWN)
        advanceUntilIdle()
        assertNotEquals(oldState, viewModel.uiState)
        val expectedRootFolder =
            FolderTestUtil.root.moveSubfolder(FolderTestUtil.child, Folder.MoveDirection.DOWN)
        assertRootFolderEquals(expectedRootFolder)
    }

    @Test
    fun testSaveEditedSubfolder() = runTest {
        assertDoesNothing {
            viewModel.saveEditedSubfolder()
        }

        viewModel.startEditingSubfolder(FolderTestUtil.root)
        viewModel.editSubfolderName("New name")
        assertDoesNothing {
            viewModel.saveEditedSubfolder()
            advanceUntilIdle()
        }

        viewModel.startEditingSubfolder(null)
        viewModel.editSubfolderName("New subfolder")
        viewModel.saveEditedSubfolder()
        advanceUntilIdle()
        var expectedRootFolder = FolderTestUtil.root.createSubfolder(Folder("New subfolder"))
        assertRootFolderEquals(expectedRootFolder)

        viewModel.startEditingSubfolder(FolderTestUtil.child)
        viewModel.editSubfolderName("New name")
        viewModel.saveEditedSubfolder()
        advanceUntilIdle()
        val expectedSubfolders = expectedRootFolder.subfolders.toMutableList()
        expectedSubfolders[0] = expectedSubfolders[0].rename("New name")
        expectedRootFolder = expectedRootFolder.copy(subfolders = expectedSubfolders)
        assertRootFolderEquals(expectedRootFolder)
        assertNull(viewModel.uiState.subfolderEdit)
        assertNull(viewModel.uiState.userMessage)
    }

    @Test
    fun testSaveEditedSubfolderErrors() = runTest {
        viewModel.startEditingSubfolder(null)
        viewModel.saveEditedSubfolder()  // Saving with blank name
        advanceUntilIdle()
        assertNotNull(viewModel.uiState.userMessage)
        verifyBlocking(rootFolderRepository, never()) { saveRootFolder(any()) }
    }

    @Test
    fun testCancelSubfolderEdit() {
        assertDoesNothing {
            viewModel.cancelSubfolderEdit()
        }

        viewModel.startEditingSubfolder(null)
        assertNotNull(viewModel.uiState.subfolderEdit)
        viewModel.cancelSubfolderEdit()
        assertNull(viewModel.uiState.subfolderEdit)
    }

    @Test
    fun testGoToSubfolder() {
        assertEquals(FolderTestUtil.root, viewModel.uiState.path?.currentFolder)
        assertDoesNothing {
            viewModel.goToSubfolder(FolderTestUtil.grandchild)
        }
        viewModel.goToSubfolder(FolderTestUtil.child)
        assertEquals(FolderTestUtil.child, viewModel.uiState.path?.currentFolder)
    }

    @Test
    fun testGoUpOneFolder() {
        assertEquals(FolderTestUtil.root, viewModel.uiState.path?.currentFolder)
        assertDoesNothing {
            viewModel.goUpOneFolder()
        }

        viewModel.goToSubfolder(FolderTestUtil.child)
        assertEquals(FolderTestUtil.child, viewModel.uiState.path?.currentFolder)
        viewModel.goToSubfolder(FolderTestUtil.grandchild)
        assertEquals(FolderTestUtil.grandchild, viewModel.uiState.path?.currentFolder)

        viewModel.goUpOneFolder()
        assertEquals(FolderTestUtil.child, viewModel.uiState.path?.currentFolder)
        viewModel.goUpOneFolder()
        assertEquals(FolderTestUtil.root, viewModel.uiState.path?.currentFolder)
    }

    @Test
    fun testGoToWords() {

    }

    @Test
    fun testToggleSubfolderSelection() {
        assertEquals(emptySet<Folder>(), viewModel.uiState.selectedSubfolders)
        assertDoesNothing {
            viewModel.toggleSubfolderSelection(FolderTestUtil.grandchild)
        }

        viewModel.toggleSubfolderSelection(FolderTestUtil.child)
        assertEquals(setOf(FolderTestUtil.child), viewModel.uiState.selectedSubfolders)

        viewModel.toggleSubfolderSelection(FolderTestUtil.anotherChild)
        assertEquals(
            setOf(FolderTestUtil.child, FolderTestUtil.anotherChild),
            viewModel.uiState.selectedSubfolders
        )

        viewModel.toggleSubfolderSelection(FolderTestUtil.child)
        assertEquals(setOf(FolderTestUtil.anotherChild), viewModel.uiState.selectedSubfolders)

        viewModel.toggleSubfolderSelection(FolderTestUtil.anotherChild)
        assertEquals(emptySet<Folder>(), viewModel.uiState.selectedSubfolders)
    }

    @Test
    fun testDeselectAllSubfolders() {
        assertEquals(emptySet<Folder>(), viewModel.uiState.selectedSubfolders)
        assertDoesNothing {
            viewModel.deselectAllSubfolders()
        }
        viewModel.toggleSubfolderSelection(FolderTestUtil.child)
        viewModel.toggleSubfolderSelection(FolderTestUtil.anotherChild)
        assertEquals(
            setOf(FolderTestUtil.child, FolderTestUtil.anotherChild),
            viewModel.uiState.selectedSubfolders
        )

        viewModel.deselectAllSubfolders()
        assertEquals(emptySet<Folder>(), viewModel.uiState.selectedSubfolders)
    }

    @Test
    fun testUserMessageShown() {
        assertNull(viewModel.uiState.userMessage)
        assertDoesNothing {
            viewModel.userMessageShown()
        }

        viewModel.startEditingSubfolder(null)
        viewModel.saveEditedSubfolder()  // Saving subfolder with an empty name.
        assertNotNull(viewModel.uiState.userMessage)

        viewModel.userMessageShown()
        assertNull(viewModel.uiState.userMessage)
    }

    @Test
    fun testSaveRootFolderInSubfolder() = runTest {
        viewModel.goToSubfolder(FolderTestUtil.child)
        viewModel.startEditingSubfolder(null)
        viewModel.editSubfolderName("New folder in child")
        viewModel.saveEditedSubfolder()

        val expectedPath = FolderTestUtil.rootPath
            .goToSubfolder(FolderTestUtil.child)
            .updateCurrentFolder { createSubfolder(Folder("New folder in child")) }
        assertEquals(expectedPath, viewModel.uiState.path)

        advanceUntilIdle()
        verifyBlocking(rootFolderRepository) { saveRootFolder(expectedPath.rootFolder) }
    }

    private fun assertDoesNothing(block: () -> Unit) {
        val oldUiState = viewModel.uiState
        block()
        assertEquals(oldUiState, viewModel.uiState)
    }

    private fun assertRootFolderEquals(expectedRootFolder: Folder) {
        assertEquals(expectedRootFolder, viewModel.uiState.path?.rootFolder)
        verifyBlocking(rootFolderRepository, times(1)) {
            saveRootFolder(expectedRootFolder)
        }
    }
}
