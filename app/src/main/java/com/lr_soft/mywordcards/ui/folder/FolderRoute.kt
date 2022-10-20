package com.lr_soft.mywordcards.ui.folder

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lr_soft.mywordcards.R
import com.lr_soft.mywordcards.model.*
import com.lr_soft.mywordcards.ui.theme.MyWordCardsTheme

@Composable
fun FolderRoute(
    viewModel: FolderViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    FolderScreen(
        uiState = viewModel.uiState,
        snackbarHostState = snackbarHostState,
        editNewSubfolder = viewModel::editNewSubfolder,
        saveNewSubfolder = viewModel::saveNewSubfolder,
        cancelEdit = viewModel::cancelEdit,
        userMessageShown = viewModel::userMessageShown
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FolderScreen(
    uiState: FolderUiState,
    snackbarHostState: SnackbarHostState,
    editNewSubfolder: (String) -> Unit = {},
    saveNewSubfolder: () -> Unit = {},
    cancelEdit: () -> Unit = {},
    userMessageShown: () -> Unit = {},
) {
    val title = uiState.path?.currentFolder?.name ?: stringResource(R.string.app_name)

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
            editNewSubfolder = editNewSubfolder,
            saveNewSubfolder = saveNewSubfolder,
            cancelEdit = cancelEdit
        )
    }

    ShowUserMessage(
        userMessage = uiState.userMessage,
        snackbarHostState = snackbarHostState,
        userMessageShown = userMessageShown
    )

    BackHandler(uiState.isInEditMode) {
        cancelEdit()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FolderScreenContent(
    uiState: FolderUiState,
    editNewSubfolder: (String) -> Unit,
    saveNewSubfolder: () -> Unit,
    cancelEdit: () -> Unit,
    modifier: Modifier
) {
    val folders = uiState.path?.currentFolder?.subfolders
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
        uiState.newSubfolder?.let {
            val focusManager = LocalFocusManager.current
            TextField(
                value = it.name,
                onValueChange = editNewSubfolder,
                label = { Text(stringResource(R.string.new_subfolder_name)) },
                singleLine = true,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
        }
        FolderButtons(
            uiState = uiState,
            editNewSubfolder = editNewSubfolder,
            saveNewSubfolder = saveNewSubfolder,
            cancelEdit = cancelEdit,
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
    uiState: FolderUiState,
    editNewSubfolder: (String) -> Unit,
    saveNewSubfolder: () -> Unit,
    cancelEdit: () -> Unit,
    modifier: Modifier
) {
    if (uiState.newSubfolder != null) {
        Button(onClick = saveNewSubfolder, modifier = modifier) {
            Text(text = stringResource(R.string.save))
        }
        Button(onClick = cancelEdit, modifier = modifier) {
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
            onClick = { editNewSubfolder("") },
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

@Composable
private fun ShowUserMessage(
    userMessage: String?,
    snackbarHostState: SnackbarHostState,
    userMessageShown: () -> Unit
) {
    if (userMessage == null) {
        return
    }
    LaunchedEffect(userMessage) {
        snackbarHostState.showSnackbar(userMessage)
        userMessageShown()
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
            uiState = FolderUiState(
                path = FolderPath(listOf(rootFolderPreview)),
            ),
            snackbarHostState = SnackbarHostState()
        )
    }
}