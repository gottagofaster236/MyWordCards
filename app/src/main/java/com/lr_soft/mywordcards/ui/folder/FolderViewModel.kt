package com.lr_soft.mywordcards.ui.folder

import android.content.Context
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

@HiltViewModel
@Suppress("StaticFieldLeak")
class FolderViewModel @Inject constructor(
    private val rootFolderRepository: RootFolderRepository,
    @ApplicationContext
    private val context: Context
) : ViewModel() {

    var uiState by mutableStateOf(FolderUiState())
        private set

    init {
        loadRootFolder()
    }

    fun editNewSubfolder(newName: String) {
        uiState = uiState.copy(
            newSubfolder = NewSubfolder(newName)
        )
    }

    fun saveNewSubfolder() {
        val path = uiState.path ?: return
        val subfolder = uiState.newSubfolder ?: return
        val newPath = try {
            path.createSubfolder(Folder(name = subfolder.name))
        } catch (e: IllegalArgumentException) {
            val userMessage = folderExceptionToString(e)
                ?: context.getString(R.string.could_not_save_new_subfolder)
            uiState = uiState.copy(userMessage = userMessage)
            return
        }

        uiState = uiState.copy(path = newPath, newSubfolder = null)
        saveRootFolder()
    }

    fun cancelEdit() {
        uiState = uiState.copy(
            folderRename = null,
            newSubfolder = null,
        )
    }

    fun goToSubfolder(subfolder: Folder) {
        val path = uiState.path ?: return
        uiState = uiState.copy(path = path.goToSubfolder(subfolder))
    }

    fun userMessageShown() {
        uiState = uiState.copy(userMessage = null)
    }

    fun goUpOneFolder() {
        val path = uiState.path ?: return
        uiState = uiState.copy(path = path.goUpOneFolder())
    }

    fun setDropdownMenuExpanded(expanded: Boolean) {
        uiState = uiState.copy(dropdownMenuExpanded = expanded)
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

            val rootPath = FolderPath(listOf(rootFolder))
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
