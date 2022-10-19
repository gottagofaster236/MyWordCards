package com.lr_soft.mywordcards.ui.folder

import com.lr_soft.mywordcards.model.Folder
import com.lr_soft.mywordcards.model.FolderPath

data class FolderViewUiState(
    val currentPath: FolderPath? = null,
    val isInEditMode: Boolean = false,
    val ongoingFolderRename: OngoingFolderRename? = null,
    val ongoingFolderCreation: OngoingFolderCreation? = null
)

data class OngoingFolderRename(
    val folder: Folder?,
    val newName: String
)

data class OngoingFolderCreation(
    val newName: String
)
