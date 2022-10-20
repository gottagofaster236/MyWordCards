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

        @JvmStatic
        private fun <T> hasDuplicates(list: List<T>): Boolean {
            return list.size != list.distinct().size
        }
    }
}

class InvalidFolderNameException : IllegalArgumentException()
class DuplicateWordPairsException : IllegalArgumentException()
class DuplicateSubfoldersException : IllegalArgumentException()
