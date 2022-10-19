package com.lr_soft.mywordcards.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class GameResults(
    val correctAnswers: Int,
    val totalWordsShown: Int
) {
    init {
        check(totalWordsShown >= 0 && correctAnswers >= 0 && correctAnswers <= totalWordsShown)
    }

    val successRatio: Float = correctAnswers.toFloat() / totalWordsShown
}
