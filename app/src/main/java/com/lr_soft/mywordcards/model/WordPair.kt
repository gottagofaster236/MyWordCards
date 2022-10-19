package com.lr_soft.mywordcards.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class WordPair(
    val first: Word,
    val second: Word
)
