package com.lr_soft.mywordcards.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lr_soft.mywordcards.ui.folder.FolderRoute

@Composable
fun MyWordCardsNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = MyWordCardsDestinations.FOLDER,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(MyWordCardsDestinations.FOLDER) {
            FolderRoute()
        }
    }
}