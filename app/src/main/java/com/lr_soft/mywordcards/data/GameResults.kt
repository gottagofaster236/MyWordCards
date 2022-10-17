package com.lr_soft.mywordcards.data

import kotlinx.serialization.Serializable

@Serializable
data class GameResults(
    val totalWordsShown: Int,
    val correctAnswers: Int
)
