package com.lr_soft.mywordcards.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class Word(
    val word: String,
    val languageCode: Language
)
