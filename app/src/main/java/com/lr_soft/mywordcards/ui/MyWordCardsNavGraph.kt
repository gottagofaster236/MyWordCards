package com.lr_soft.mywordcards.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.os.bundleOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lr_soft.mywordcards.ui.folder.FolderRoute
import com.lr_soft.mywordcards.ui.words.WordsRoute

const val FOLDERS_KEY = "folders"

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
            FolderRoute(
                goToWords = { folders ->
                    val bundle = bundleOf(FOLDERS_KEY to ArrayList(folders))
                    val destination = navController.findDestination(MyWordCardsDestinations.WORDS)
                    checkNotNull(destination)
                    navController.navigate(destination.id, bundle)
                }
            )
        }
        composable(MyWordCardsDestinations.WORDS) { backStackEntry ->
            val folders = backStackEntry.arguments?.getStringArrayList(FOLDERS_KEY)
            checkNotNull(folders) { "$FOLDERS_KEY must be provided" }
            WordsRoute(folders)
        }
    }
}
