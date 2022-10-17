package com.lr_soft.mywordcards.model

import kotlinx.serialization.Serializable

@Serializable
data class WordPair(
    val first: Word,
    val second: Word
)
