package com.lr_soft.mywordcards.ui

import androidx.compose.runtime.Composable
import com.lr_soft.mywordcards.ui.theme.MyWordCardsTheme

@Composable
fun MyWordCardsApp() {
    MyWordCardsTheme {
        MyWordCardsNavGraph()
    }
}
