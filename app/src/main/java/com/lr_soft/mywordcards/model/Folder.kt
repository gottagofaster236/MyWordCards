package com.lr_soft.mywordcards.model

import android.content.Context
import androidx.compose.runtime.Immutable
import com.lr_soft.mywordcards.R
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class Folder(
    val name: String,
    val wordPairs: List<WordPair>,
    val subfolders: List<Folder>,
    val lastGameResults: GameResults?,
    val incorrectlyGuessedWordPairs: List<WordPair>
) {

    init {
        checkUnique(wordPairs)
        checkUnique(subfolders)
        checkUnique(incorrectlyGuessedWordPairs)
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
            return Folder(
                name = context.getString(R.string.root_folder_name),
                wordPairs = emptyList(),
                subfolders = emptyList(),
                lastGameResults = null,
                incorrectlyGuessedWordPairs = emptyList()
            )
        }

        @JvmStatic
        private fun <T> checkUnique(list: List<T>) {
            check(list.size == list.distinct().size)
        }
    }
}
