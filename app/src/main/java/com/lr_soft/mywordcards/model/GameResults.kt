package com.lr_soft.mywordcards.model

import kotlinx.serialization.Serializable

@Serializable
data class GameResults(
    val correctAnswers: Int,
    val totalWordsShown: Int
) {
    init {
        check(totalWordsShown >= 0 && correctAnswers >= 0 && correctAnswers <= totalWordsShown)
    }
}
