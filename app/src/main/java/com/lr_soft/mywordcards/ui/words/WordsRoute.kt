package com.lr_soft.mywordcards.ui.words

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun WordsRoute(folders: List<String>) {
    Text("Words from ${folders.joinToString(", ")}")
}