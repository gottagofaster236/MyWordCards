package com.lr_soft.mywordcards.ui.folder

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lr_soft.mywordcards.data.RootFolderRepository
import com.lr_soft.mywordcards.model.FolderPath
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FolderViewModel @Inject constructor(
    private val rootFolderRepository: RootFolderRepository
) : ViewModel() {

    var uiState by mutableStateOf(FolderViewUiState())
        private set

    init {
        loadRootFolder()
    }

    fun onCreateSubfolder(currentName: String) {
        uiState = uiState.copy(
            ongoingFolderCreation = OngoingFolderCreation(currentName)
        )
    }

    private fun loadRootFolder() {
        viewModelScope.launch {
            val rootPath = FolderPath(listOf(rootFolderRepository.loadRootFolder()))
            uiState = uiState.copy(currentPath = rootPath)
        }
    }
}
