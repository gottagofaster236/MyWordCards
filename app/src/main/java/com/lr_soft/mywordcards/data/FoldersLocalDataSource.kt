package com.lr_soft.mywordcards.data

import android.content.Context
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoldersLocalDataSource @Inject constructor(
    context: Context
) {
    private val filesDir: File? = context.filesDir
}