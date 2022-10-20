package com.lr_soft.mywordcards.ui.folder

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lr_soft.mywordcards.R
import com.lr_soft.mywordcards.model.*
import com.lr_soft.mywordcards.ui.theme.MyWordCardsTheme

@Composable
fun FolderDestination(
    viewModel: FolderViewModel = hiltViewModel()
) {
    FolderScreen(
        uiState = viewModel.uiState,
        onCreateSubfolder = viewModel::onCreateSubfolder,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FolderScreen(
    uiState: FolderViewUiState,
    onCreateSubfolder: (String) -> Unit
) {
    val title = uiState.currentPath?.currentFolder?.name ?: stringResource(R.string.app_name)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Filled.Folder,
                        contentDescription = null,
                        modifier = Modifier.size(30.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPaddingModifier ->
        FolderScreenContent(
            uiState = uiState,
            modifier = Modifier.padding(innerPaddingModifier),
            onCreateSubfolder = onCreateSubfolder
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FolderScreenContent(
    uiState: FolderViewUiState,
    onCreateSubfolder: (String) -> Unit,
    modifier: Modifier
) {
    val folders = uiState.currentPath?.currentFolder?.subfolders
        ?: emptyList()
    Column(
        modifier = modifier.padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(folders) { folder ->
                FolderItem(
                    folder = folder,
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
        uiState.ongoingFolderCreation?.let {
            TextField(
                value = it.newName,
                onValueChange = onCreateSubfolder,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
        FolderButtons(
            uiState = uiState,
            onCreateSubfolder = onCreateSubfolder,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun FolderItem(
    folder: Folder,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Folder,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(30.dp)
            )
            Text(folder.name, Modifier.padding(horizontal = 10.dp))
            Spacer(Modifier.weight(1f))
            FolderGameResults(
                gameResults = folder.lastGameResults,
                modifier = Modifier.padding(horizontal = 5.dp)
            )
        }
    }
}

@Composable
private fun FolderGameResults(gameResults: GameResults?, modifier: Modifier) {
    if (gameResults == null || gameResults.totalWordsShown == 0) {
        return
    }

    val successRatio = gameResults.successRatio
    val color = Color(
        red = (255 * (1 - successRatio)).toInt(),
        green = (255 * successRatio).toInt(),
        blue = 0
    )
    Surface(
        modifier = modifier.wrapContentSize(),
        shape = CircleShape,
        color = color,
    ) {
        Text(
            text = "${gameResults.correctAnswers}/${gameResults.totalWordsShown}",
            color = Color.White,
            modifier = Modifier.padding(5.dp)
        )
    }
}

@Composable
private fun FolderButtons(
    uiState: FolderViewUiState,
    onCreateSubfolder: (String) -> Unit,
    modifier: Modifier
) {
    if (uiState.ongoingFolderCreation != null) {
        Button(onClick = {}, modifier = modifier) {
            Text(text = stringResource(R.string.add_subfolder))
        }
        Button(onClick = {}, modifier = modifier) {
            Text(text = stringResource(R.string.cancel))
        }
    } else {
        Button(
            onClick = {},
            modifier = modifier
        ) {
            Text(text = stringResource(R.string.add_words))
        }
        Button(
            onClick = { onCreateSubfolder("") },
            modifier = modifier
        ) {
            Text(text = stringResource(R.string.add_subfolder))
        }
        Button(
            onClick = {},
            modifier = modifier
        ) {
            Text(text = stringResource(R.string.edit_subfolders))
        }
    }
}

// Data for preview.

private val englishGermanWordPairs = listOf(
    WordPair(Word("hello", Language.EN), Word("hallo", Language.DE)),
    WordPair(Word("goodbye", Language.EN), Word("Auf Wiedersehen", Language.DE))
)

private val folderPreview = Folder(
    name = "en to de",
    wordPairs = englishGermanWordPairs,
    subfolders = emptyList(),
    lastGameResults = GameResults(1, 1),
    incorrectlyGuessedWordPairs = listOf(englishGermanWordPairs[0])
)

private val rootFolderPreview = Folder(
    name = "root",
    wordPairs = englishGermanWordPairs,
    subfolders = listOf(folderPreview, folderPreview.copy(name = "en to de 2")),
    lastGameResults = null,
    incorrectlyGuessedWordPairs = englishGermanWordPairs
)

@Preview
@Composable
private fun FolderItemPreview() {
    MyWordCardsTheme {
        FolderItem(folder = folderPreview)
    }
}

@Preview
@Composable
private fun FolderPreview() {
    MyWordCardsTheme {
        FolderScreen(
            uiState = FolderViewUiState(
                currentPath = FolderPath(listOf(rootFolderPreview)),
            ),
            onCreateSubfolder = {}
        )
    }
}
