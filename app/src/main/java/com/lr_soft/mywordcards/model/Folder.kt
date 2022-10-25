package com.lr_soft.mywordcards.model

import android.content.Context
import androidx.compose.runtime.Immutable
import com.lr_soft.mywordcards.R
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class Folder(
    val name: String,
    val wordPairs: List<WordPair> = emptyList(),
    val subfolders: List<Folder> = emptyList(),
    val lastGameResults: GameResults? = null,
    val incorrectlyGuessedWordPairs: List<WordPair> = emptyList()
) {

    init {
        if (name.isBlank()) {
            throw InvalidFolderNameException()
        }
        if (hasDuplicates(wordPairs) || hasDuplicates(incorrectlyGuessedWordPairs)) {
            throw DuplicateWordPairsException()
        }
        if (hasDuplicates(subfolders)) {
            throw DuplicateSubfoldersException()
        }
    }

    private fun <T> hasDuplicates(list: List<T>): Boolean {
        return list.size != list.distinct().size
    }

    fun createSubfolder(subfolder: Folder): Folder {
        return copy(subfolders = subfolders + listOf(subfolder))
    }

    /**
     * Move a [subfolder] [up] or down in the subfolder list,
     * or do nothing if there's nowhere to move the subfolder.
     */
    fun moveSubfolder(subfolder: Folder, up: Boolean): Folder {
        val index = subfolders.indexOf(subfolder)
        require(index != -1) { "Must be a subfolder" }

        val indexToSwapWith = index + (if (up) -1 else 1)
        if (indexToSwapWith !in subfolders.indices) {
            return this
        }

        val resultSubfolders = subfolders.toMutableList()
        val tmp = resultSubfolders[index]
        resultSubfolders[index] = resultSubfolders[indexToSwapWith]
        resultSubfolders[indexToSwapWith] = tmp
        return copy(subfolders = resultSubfolders)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Folder

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    companion object {
        @JvmStatic
        fun emptyRootFolder(context: Context): Folder {
            return Folder(name = context.getString(R.string.root_folder_name))
        }
    }
}

class InvalidFolderNameException : IllegalArgumentException()
class DuplicateWordPairsException : IllegalArgumentException()
class DuplicateSubfoldersException : IllegalArgumentException()
