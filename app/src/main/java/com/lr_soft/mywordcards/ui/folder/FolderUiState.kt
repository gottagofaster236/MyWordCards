package com.lr_soft.mywordcards.ui.folder

import com.lr_soft.mywordcards.model.Folder
import com.lr_soft.mywordcards.model.FolderPath
import com.lr_soft.mywordcards.model.WordPair

data class FolderUiState(
    val path: FolderPath? = null,
    val newSubfolder: NewSubfolder? = null,
    val newWordPair: WordPair? = null,
    val subfolderRename: SubfolderRename? = null,
    val isEditingSubfolders: Boolean = false,
    val dropdownMenuExpanded: Boolean = false,
    val userMessage: String? = null,
) {
    val isInEditMode: Boolean
        get() {
            return newSubfolder != null || newWordPair != null ||
                    subfolderRename != null || isEditingSubfolders
        }
}

data class NewSubfolder(
    val name: String = ""
)

data class SubfolderRename(
    val folder: Folder?,
    val newName: String
)
