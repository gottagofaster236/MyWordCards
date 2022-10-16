package com.lr_soft.mywordcards.data

import kotlinx.serialization.Serializable

@Serializable
data class Word(
    val word: String,
    val languageCode: Language
)
