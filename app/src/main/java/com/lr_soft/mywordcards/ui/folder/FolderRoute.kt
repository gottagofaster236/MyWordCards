package com.lr_soft.mywordcards.ui.folder

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
        startEditingSubfolder = viewModel::startEditingSubfolder,
        editSubfolderName = viewModel::editSubfolderName,
        showDeleteSubfolderDialog = viewModel::showDeleteSubfolderDialog,
        dismissDeleteSubfolder = viewModel::dismissDeleteSubfolder,
        confirmDeleteSubfolder = viewModel::confirmDeleteSubfolder,
        moveSubfolder = viewModel::moveSubfolder,
        saveEditedSubfolder = viewModel::saveEditedSubfolder,
        cancelSubfolderEdit = viewModel::cancelSubfolderEdit,
        goToSubfolder = viewModel::goToSubfolder,
        goUpOneFolder = viewModel::goUpOneFolder,
        goToWords = viewModel::goToWords,
        toggleSubfolderSelection = viewModel::toggleSubfolderSelection,
        deselectAllSubfolders = viewModel::deselectAllSubfolders,
        userMessageShown = viewModel::userMessageShown,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FolderScreen(
    uiState: FolderUiState,
    snackbarHostState: SnackbarHostState,
    startEditingSubfolder: (Folder?) -> Unit = {},
    editSubfolderName: (String) -> Unit = {},
    showDeleteSubfolderDialog: () -> Unit = {},
    dismissDeleteSubfolder: () -> Unit = {},
    confirmDeleteSubfolder: () -> Unit = {},
    moveSubfolder: (Folder.MoveDirection) -> Unit = {},
    saveEditedSubfolder: () -> Unit = {},
    cancelSubfolderEdit: () -> Unit = {},
    goToSubfolder: (Folder) -> Unit = {},
    goUpOneFolder: () -> Unit = {},
    goToWords: () -> Unit = {},
    toggleSubfolderSelection: (Folder) -> Unit = {},
    deselectAllSubfolders: () -> Unit = {},
    userMessageShown: () -> Unit = {},
) {
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            FolderTopBar(
                uiState = uiState,
                createNewSubfolder = { startEditingSubfolder(null) },
                goUpOneFolder = goUpOneFolder,
            )
        }
    ) { innerPaddingModifier ->
        FolderScreenContent(
            uiState = uiState, modifier = Modifier.padding(innerPaddingModifier),
            startEditingSubfolder = startEditingSubfolder, editSubfolderName = editSubfolderName,
            showDeleteSubfolderDialog = showDeleteSubfolderDialog, moveSubfolder = moveSubfolder,
            saveEditedSubfolder = saveEditedSubfolder, cancelSubfolderEdit = cancelSubfolderEdit,
            goToSubfolder = goToSubfolder, goToWords = goToWords,
            toggleSubfolderSelection = toggleSubfolderSelection,
        )
    }

    UserMessage(
        userMessage = uiState.userMessage,
        snackbarHostState = snackbarHostState,
        userMessageShown = userMessageShown
    )

    if (uiState.subfolderEdit != null && uiState.subfolderEdit.showDeleteDialog) {
        DeleteSubfolderDialog(
            subfolderName = uiState.subfolderEdit.newName,
            dismissDeleteSubfolder = dismissDeleteSubfolder,
            confirmDeleteSubfolder = confirmDeleteSubfolder
        )
    }

    BackHandler(uiState.subfolderEdit != null || uiState.selectedSubfolders.isNotEmpty()) {
        if (uiState.subfolderEdit != null) {
            cancelSubfolderEdit()
        } else {
            deselectAllSubfolders()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FolderTopBar(
    uiState: FolderUiState,
    createNewSubfolder: () -> Unit,
    goUpOneFolder: () -> Unit,
) {
    val title = uiState.path?.currentFolder?.name ?: stringResource(R.string.app_name)
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        navigationIcon = {
            NavigationIcon(
                uiState = uiState,
                goUpOneFolder = goUpOneFolder,
            )
        },
        actions = {
            CustomIconButton(
                imageVector = Icons.Default.CreateNewFolder,
                modifier = Modifier.padding(horizontal = 5.dp),
                onClick = createNewSubfolder,
                contentDescription = stringResource(R.string.create_new_subfolder)
            )
        },
        colors = centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
    )
}

@Composable
private fun NavigationIcon(
    uiState: FolderUiState,
    goUpOneFolder: () -> Unit,
) {
    val path = uiState.path
    if (path != null && path.canGoUpOneFolder) {
        CustomIconButton(
            imageVector = Icons.Default.ArrowBack,
            onClick = goUpOneFolder,
            contentDescription = stringResource(R.string.go_up_one_folder),
        )
    } else {
        CustomIconButton(Icons.Default.Folder)
    }
}

@Composable
private fun CustomIconButton(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    size: Dp = 30.dp,
    onClick: () -> Unit = {},
    contentDescription: String? = null,
    tint: Color = MaterialTheme.colorScheme.onPrimary
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(size)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier.size(size),
            tint = tint
        )
    }
}

@Composable
private fun FolderScreenContent(
    uiState: FolderUiState,
    modifier: Modifier,
    startEditingSubfolder: (Folder?) -> Unit = {},
    editSubfolderName: (String) -> Unit = {},
    showDeleteSubfolderDialog: () -> Unit = {},
    moveSubfolder: (Folder.MoveDirection) -> Unit = {},
    saveEditedSubfolder: () -> Unit = {},
    cancelSubfolderEdit: () -> Unit = {},
    goToSubfolder: (Folder) -> Unit = {},
    goToWords: () -> Unit = {},
    toggleSubfolderSelection: (Folder) -> Unit = {}
) {
    val folders = uiState.path?.currentFolder?.subfolders
        ?: emptyList()
    Column(
        modifier = modifier.padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(folders) { folder ->
                val onClick = if (uiState.selectedSubfolders.isEmpty()){
                    goToSubfolder
                } else {
                    toggleSubfolderSelection
                }
                FolderItem(
                    folder = folder, editMode = folder == uiState.subfolderEdit?.subfolder,
                    isSelected = folder in uiState.selectedSubfolders,
                    startEditingSubfolder = { startEditingSubfolder(folder) },
                    showDeleteSubfolderDialog = showDeleteSubfolderDialog, moveSubfolder = moveSubfolder,
                    onClick = { onClick(folder) }, onLongClick = { toggleSubfolderSelection(folder) },
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
        FolderBottomControls(
            uiState = uiState, editSubfolderName = editSubfolderName,
            saveEditedSubfolder = saveEditedSubfolder, cancelSubfolderEdit = cancelSubfolderEdit,
            goToWords = goToWords, modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun FolderItem(
    folder: Folder,
    editMode: Boolean,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    startEditingSubfolder: () -> Unit = {},
    showDeleteSubfolderDialog: () -> Unit = {},
    moveSubfolder: (Folder.MoveDirection) -> Unit = {},
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {}
) {
    FolderItemContainer(
        isSelected = isSelected, onClick = onClick,
        onLongClick = onLongClick, modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            CustomIconButton(
                imageVector = Icons.Default.Folder,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = folder.name,
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .weight(1f),
                maxLines = 1
            )
            if (editMode) {
                FolderEditModeButtons(
                    showDeleteSubfolderDialog = showDeleteSubfolderDialog,
                    moveSubfolder = moveSubfolder
                )
            } else {
                FolderNormalButtons(startEditingSubfolder = startEditingSubfolder)
            }
            FolderGameResults(
                gameResults = folder.lastGameResults,
                modifier = Modifier.padding(start = 5.dp)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FolderItemContainer(
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    val modifierClickable = modifier.combinedClickable(
        onClick = onClick,
        onLongClick = onLongClick
    )
    if (isSelected) {
        Card(modifier = modifierClickable) { content() }
    } else {
        ElevatedCard(modifier = modifierClickable) { content() }
    }
}

@Composable
private fun FolderEditModeButtons(
    showDeleteSubfolderDialog: () -> Unit,
    moveSubfolder: (Folder.MoveDirection) -> Unit,
) {
    CustomIconButton(
        imageVector = Icons.Default.Delete,
        onClick = showDeleteSubfolderDialog,
        contentDescription = stringResource(R.string.delete_folder),
        tint = MaterialTheme.colorScheme.primary,
    )
    CustomIconButton(
        imageVector = Icons.Default.ArrowUpward,
        onClick = { moveSubfolder(Folder.MoveDirection.UP) },
        contentDescription = stringResource(R.string.move_up),
        tint = MaterialTheme.colorScheme.primary,
    )
    CustomIconButton(
        imageVector = Icons.Default.ArrowDownward,
        onClick = { moveSubfolder(Folder.MoveDirection.DOWN) },
        contentDescription = stringResource(R.string.move_down),
        tint = MaterialTheme.colorScheme.primary,
    )
}

@Composable
private fun FolderNormalButtons(startEditingSubfolder: () -> Unit) {
    CustomIconButton(
        imageVector = Icons.Default.Edit,
        onClick = startEditingSubfolder,
        contentDescription = stringResource(R.string.edit_folder),
        tint = MaterialTheme.colorScheme.primary,
    )
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
private fun FolderBottomControls(
    uiState: FolderUiState,
    editSubfolderName: (String) -> Unit,
    saveEditedSubfolder: () -> Unit,
    cancelSubfolderEdit: () -> Unit,
    goToWords: () -> Unit,
    modifier: Modifier
) {
    if (uiState.subfolderEdit != null) {
        SubfolderEditControls(
            subfolderEdit = uiState.subfolderEdit,
            editSubfolderName = editSubfolderName,
            saveEditedSubfolder = saveEditedSubfolder,
            cancelSubfolderEdit = cancelSubfolderEdit,
            modifier = modifier
        )
    } else {
        Button(onClick = goToWords, modifier = modifier) {
            val text = if (uiState.selectedSubfolders.isNotEmpty()) {
                stringResource(R.string.go_to_words_in_selected_subfolders)
            } else {
                stringResource(R.string.go_to_words_in_folder)
            }
            Text(text)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubfolderEditControls(
    subfolderEdit: SubfolderEdit,
    editSubfolderName: (String) -> Unit,
    saveEditedSubfolder: () -> Unit,
    cancelSubfolderEdit: () -> Unit,
    modifier: Modifier
) {
    val focusManager = LocalFocusManager.current
    TextField(
        value = subfolderEdit.newName,
        onValueChange = { editSubfolderName(it) },
        label = {
            Text(stringResource(R.string.new_subfolder_name))
        },
        singleLine = true,
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        modifier = modifier.padding(5.dp)
    )
    Button(onClick = saveEditedSubfolder, modifier = modifier) {
        Text(text = stringResource(R.string.save))
    }
    Button(onClick = cancelSubfolderEdit, modifier = modifier) {
        Text(text = stringResource(R.string.cancel))
    }
}

@Composable
private fun UserMessage(
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

@Composable
private fun DeleteSubfolderDialog(
    subfolderName: String,
    dismissDeleteSubfolder: () -> Unit,
    confirmDeleteSubfolder: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = dismissDeleteSubfolder,
        title = { Text(stringResource(R.string.confirm_delete)) },
        text = { Text(stringResource(R.string.click_delete_to_confirm, subfolderName)) },
        confirmButton = {
            Button(onClick = confirmDeleteSubfolder) {
                Text("Delete")
            }
        },
        dismissButton = {
            Button(onClick = dismissDeleteSubfolder) {
                Text("Cancel")
            }
        }
    )
}

// Data for preview.

private val englishGermanWordPairs = listOf(
    WordPair(Word("hello", Language.EN), Word("hallo", Language.DE)),
    WordPair(Word("goodbye", Language.EN), Word("Auf Wiedersehen", Language.DE))
)

private val folderPreview = Folder(
    name = "very long text ".repeat(10),
    wordPairs = englishGermanWordPairs,
    subfolders = emptyList(),
    lastGameResults = GameResults(1, 1),
    incorrectlyGuessedWordPairs = listOf(englishGermanWordPairs[0])
)

private val rootFolderPreview = Folder(
    name = "root",
    wordPairs = englishGermanWordPairs,
    subfolders = listOf(folderPreview, folderPreview.copy(name = "en to de"),
        folderPreview.copy(name = "fr to en")),
    lastGameResults = null,
    incorrectlyGuessedWordPairs = englishGermanWordPairs
)

@Preview("Light")
@Preview("Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun FolderItemPreview() {
    MyWordCardsTheme {
        FolderItem(
            folder = folderPreview,
            editMode = false,
            isSelected = false
        )
    }
}

@Preview("Light")
@Preview("Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun FolderItemEditPreview() {
    MyWordCardsTheme {
        FolderItem(
            folder = folderPreview,
            editMode = true,
            isSelected = false
        )
    }
}

@Preview("Light")
@Preview("Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun FolderItemSelectedPreview() {
    MyWordCardsTheme {
        FolderItem(
            folder = folderPreview,
            editMode = false,
            isSelected = true
        )
    }
}

@Preview("Light")
@Preview("Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun FolderPreview() {
    MyWordCardsTheme {
        FolderScreen(
            uiState = FolderUiState(
                path = FolderPath(rootFolderPreview),
                selectedSubfolders = setOf(rootFolderPreview.subfolders.last()),
                userMessage = "Toast"
            ),
            snackbarHostState = SnackbarHostState()
        )
    }
}
