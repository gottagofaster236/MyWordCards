package com.lr_soft.mywordcards.model

import kotlinx.serialization.Serializable

@Serializable
data class GameResults(
    val totalWordsShown: Int,
    val correctAnswers: Int
)
