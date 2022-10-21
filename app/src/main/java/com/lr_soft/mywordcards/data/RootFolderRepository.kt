package com.lr_soft.mywordcards.data

import android.content.Context
import com.lr_soft.mywordcards.model.Folder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

private const val FILENAME = "root_folder.json"

@Singleton
class RootFolderRepository @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val externalScope: CoroutineScope
) {

    private val file = File(context.filesDir, FILENAME)
    private val fileMutex = Mutex()

    suspend fun loadRootFolder(): Folder {
        return withContext(externalScope.coroutineContext + Dispatchers.IO) {
            val text = fileMutex.withLock {
                if (file.exists()) {
                    file.readText()
                } else {
                    ""
                }
            }

            if (text.isNotEmpty()) {
                Json.decodeFromString(text)
            } else {
                Folder.emptyRootFolder(context)
            }
        }
    }

    suspend fun saveRootFolder(rootFolder: Folder) {
        withContext(externalScope.coroutineContext + Dispatchers.IO) {
            val text = fileMutex.withLock {
                Json.encodeToString(rootFolder)
            }
            file.writeText(text)
        }
    }
}
