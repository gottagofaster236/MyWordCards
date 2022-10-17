package com.lr_soft.mywordcards.model

import android.content.Context
import com.lr_soft.mywordcards.R
import kotlinx.serialization.Serializable

@Serializable
data class Folder(
    val name: String,
    val wordPairs: List<WordPair>,
    val subfolders: List<Folder>,
    val lastGameResults: GameResults?,
    val incorrectlyGuessedWordPairs: Set<WordPair>
) {

    init {
        val childNames = subfolders.map(Folder::name)
        val areChildNamesUnique = childNames.size == childNames.distinct().size
        check(areChildNamesUnique)
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

    private companion object {
        @JvmStatic
        fun emptyRootFolder(context: Context) {
            Folder(
                name = context.getString(R.string.root_folder_name),
                wordPairs = emptyList(),
                subfolders = emptyList(),
                lastGameResults = null,
                incorrectlyGuessedWordPairs = emptySet()
            )
        }
    }
}
