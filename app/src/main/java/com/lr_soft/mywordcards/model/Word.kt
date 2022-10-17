package com.lr_soft.mywordcards.model

import kotlinx.serialization.Serializable

@Serializable
data class Word(
    val word: String,
    val languageCode: Language
)
