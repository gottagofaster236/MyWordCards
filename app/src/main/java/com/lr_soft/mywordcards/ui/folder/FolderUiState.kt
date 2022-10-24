package com.lr_soft.mywordcards.ui.folder

import com.lr_soft.mywordcards.model.Folder
import com.lr_soft.mywordcards.model.FolderPath

data class FolderUiState(
    val path: FolderPath? = null,
    val newSubfolder: NewSubfolder? = null,
    val folderRename: FolderRename? = null,
    val userMessage: String? = null,
    val dropdownMenuExpanded: Boolean = false,
) {
    val isInEditMode: Boolean
        get() = newSubfolder != null || folderRename != null
}

data class NewSubfolder(
    val name: String = ""
)

data class FolderRename(
    val folder: Folder?,
    val newName: String
)
