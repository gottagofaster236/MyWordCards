package com.lr_soft.mywordcards.ui.folder

import android.content.Context
import androidx.annotation.UiThread
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lr_soft.mywordcards.R
import com.lr_soft.mywordcards.data.RootFolderRepository
import com.lr_soft.mywordcards.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

/**
 * All the methods for the view model assume that
 * the UI displayed on the screen may be outdated. So, for example, the UI may call
 * a ViewModel method for a deleted folder. Nothing should happen in such case.
 */
@HiltViewModel
@Suppress("StaticFieldLeak")
class FolderViewModel @Inject constructor(
    private val rootFolderRepository: RootFolderRepository,
    @ApplicationContext
    private val context: Context
) : ViewModel() {

    private var _uiState by mutableStateOf(FolderUiState())

    var uiState: FolderUiState
        get() = _uiState
        @UiThread
        private set(value) {
            /**
             * If the `path` was `null`, that means we loaded it with [loadRootFolder]
             * for the first time. No need to save it to disk again.
             */
            val haveToSave = _uiState.path != null && _uiState.path != value.path
            _uiState = value
            if (haveToSave) {
                saveRootFolder()
            }
        }

    init {
        loadRootFolder()
    }

    /**
     * Pass `null` to create a subfolder, otherwise [subfolder] is gonna be edited.
     */
    fun startEditingSubfolder(subfolder: Folder?) {
        val currentFolderSubfolders = uiState.path?.currentFolder?.subfolders ?: return
        if (subfolder == null || subfolder in currentFolderSubfolders) {
            cancelSubfolderEdit()
            uiState = uiState.copy(
                subfolderEdit = SubfolderEdit(subfolder),
                selectedSubfolders = emptySet()
            )
        }
    }

    fun editSubfolderName(newName: String) {
        val subfolderUpdate = uiState.subfolderEdit ?: return
        uiState = uiState.copy(subfolderEdit = subfolderUpdate.copy(newName = newName))
    }

    fun showDeleteSubfolderDialog() {
        val subfolderEdit = uiState.subfolderEdit ?: return
        if (subfolderEdit.subfolder == null) {
            return
        }
        uiState = uiState.copy(subfolderEdit = subfolderEdit.copy(showDeleteDialog = true))
    }

    fun dismissDeleteSubfolder() {
        val subfolderEdit = uiState.subfolderEdit ?: return
        uiState = uiState.copy(subfolderEdit = subfolderEdit.copy(showDeleteDialog = false))
    }

    fun confirmDeleteSubfolder() {
        if (uiState.subfolderEdit?.showDeleteDialog != true) {
            return
        }
        val subfolder = uiState.subfolderEdit?.subfolder ?: return
        val path = uiState.path ?: return
        val newPath = path.updateCurrentFolder { deleteSubfolder(subfolder) }
        uiState = uiState.copy(path = newPath, subfolderEdit = null)
    }

    fun moveSubfolder(direction: Folder.MoveDirection) {
        val subfolder = uiState.subfolderEdit?.subfolder ?: return
        val path = uiState.path ?: return
        val newPath = path.updateCurrentFolder { moveSubfolder(subfolder, direction) }
        uiState = uiState.copy(path = newPath)
    }

    fun saveEditedSubfolder() {
        val path = uiState.path ?: return
        val edit = uiState.subfolderEdit ?: return
        val newPath = try {
            val subfolder = edit.subfolder
            if (subfolder != null) {
                path.updateSubfolder(subfolder, subfolder.rename(edit.newName))
            } else {
                path.updateCurrentFolder {
                    createSubfolder(Folder(name = edit.newName))
                }
            }
        } catch (e: IllegalArgumentException) {
            val userMessage = folderExceptionToString(e)
                ?: context.getString(R.string.could_not_save_new_subfolder)
            uiState = uiState.copy(userMessage = userMessage)
            return
        }

        uiState = uiState.copy(path = newPath, subfolderEdit = null)
    }

    fun cancelSubfolderEdit() {
        uiState = uiState.copy(
            subfolderEdit = null
        )
    }

    fun goToSubfolder(subfolder: Folder) {
        val path = uiState.path ?: return
        if (subfolder !in path.currentFolder.subfolders) {
            return
        }
        uiState = uiState.copy(
            path = path.goToSubfolder(subfolder),
            subfolderEdit = null,
            selectedSubfolders = emptySet()
        )
    }

    fun goUpOneFolder() {
        val path = uiState.path ?: return
        if (path.currentFolder != path.rootFolder) {
            uiState = uiState.copy(
                path = path.goUpOneFolder(),
                subfolderEdit = null,
                selectedSubfolders = emptySet()
            )
        }
    }

    fun toggleSubfolderSelection(subfolder: Folder) {
        val path = uiState.path ?: return
        if (subfolder !in path.currentFolder.subfolders) {
            return
        }
        val newSelectedSubfolders = uiState.selectedSubfolders.toMutableSet()
        if (subfolder in newSelectedSubfolders) {
            newSelectedSubfolders.remove(subfolder)
        } else {
            newSelectedSubfolders.add(subfolder)
        }
        uiState = uiState.copy(selectedSubfolders = newSelectedSubfolders, subfolderEdit = null)
    }

    fun deselectAllSubfolders() {
        uiState = uiState.copy(selectedSubfolders = emptySet())
    }

    fun userMessageShown() {
        uiState = uiState.copy(userMessage = null)
    }

    private fun folderExceptionToString(e: Exception): String? {
        return when (e) {
            is InvalidFolderNameException -> context.getString(R.string.invalid_folder_name)
            is DuplicateWordPairsException -> context.getString(R.string.duplicate_word_pairs)
            is DuplicateSubfoldersException -> context.getString(R.string.duplicate_subfolders)
            else -> null
        }
    }

    private fun loadRootFolder() {
        viewModelScope.launch {
            val rootFolder = try {
                rootFolderRepository.loadRootFolder()
            } catch (e: Exception) {
                handleLoadSaveException(e) {
                    uiState = uiState.copy(
                        userMessage = context.getString(R.string.could_not_load_root_folder)
                    )
                    Folder.emptyRootFolder(context)
                }
            }

            val rootPath = FolderPath(rootFolder)
            uiState = uiState.copy(path = rootPath)
        }
    }

    private fun saveRootFolder() {
        val rootFolder = uiState.path?.rootFolder ?: return
        viewModelScope.launch {
            try {
                rootFolderRepository.saveRootFolder(rootFolder)
            } catch (e: Exception) {
                handleLoadSaveException(e) {
                    uiState = uiState.copy(
                        userMessage = context.getString(R.string.could_not_save_root_folder)
                    )
                }
            }
        }
    }

    private fun <T> handleLoadSaveException(e: Exception, onLoadSaveException: () -> T): T {
        return when (e) {
            is IOException, is IllegalArgumentException -> onLoadSaveException()
            else -> throw e
        }
    }
}
