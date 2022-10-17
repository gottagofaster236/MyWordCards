package com.lr_soft.mywordcards.data

data class FolderPath(
    val folders: List<Folder>
) {
    init {
        check(folders.isNotEmpty()) { "Can't go up from the root folder" }
    }

    val currentFolder: Folder
        get() = folders.last()

    fun goUpOneFolder(): FolderPath {
        return FolderPath(folders.dropLast(1))
    }

    fun goToSubfolder(folder: Folder): FolderPath {
        check(folder in currentFolder.subfolders) {
            "Can only go to a subfolder"
        }
        return FolderPath(folders.toMutableList().apply {
            add(folder)
        })
    }

    fun updateCurrentFolder(newFolder: Folder): FolderPath {
        val newFolders = folders.toMutableList()
        newFolders[newFolders.lastIndex] = newFolder
        for (i in newFolders.size - 2 downTo 0) {
            val oldSubfolders = folders[i].subfolders
            val childIndex = oldSubfolders.indexOf(folders[i + 1])
            val newSubfolders = oldSubfolders.toMutableList().apply {
                this[childIndex] = newFolders[i + 1]
            }
            newFolders[i] = folders[i].copy(subfolders = newSubfolders)
        }
        return FolderPath(newFolders)
    }
}
