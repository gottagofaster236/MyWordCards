package com.lr_soft.mywordcards.ui.folder

import androidx.compose.runtime.Immutable
import com.lr_soft.mywordcards.model.Folder
import com.lr_soft.mywordcards.model.FolderPath

@Immutable
data class FolderUiState(
    val path: FolderPath? = null,
    val subfolderEdit: SubfolderEdit? = null,
    val selectedSubfolders: Set<Folder> = emptySet(),
    val userMessage: String? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FolderUiState

        if (path != other.path) return false
        if (subfolderEdit != other.subfolderEdit) return false
        if (selectedSubfolders != other.selectedSubfolders) return false
        if (userMessage != other.userMessage) return false

        return true
    }

    override fun hashCode(): Int = 0
}

/**
 * If [subfolder] is `null`, it's a new folder creation instead.
 */
data class SubfolderEdit(
    val subfolder: Folder?,
    val newName: String = subfolder?.name ?: "",
    val showDeleteDialog: Boolean = false,
)
