package com.lr_soft.mywordcards.data

import kotlinx.serialization.Serializable

@Serializable
data class Folder(
    val name: String,
    val wordPairs: List<WordPair>,
    val subfolders: List<Folder>,
    val lastGameResults: GameResults?,
    val incorrectlyGuessedWordPairs: Set<WordPair>
)
