package com.lr_soft.mywordcards.data

import kotlinx.serialization.Serializable

@Serializable
data class Folder(
    val name: String,
    val wordPairs: List<Word>,
    val subfolders: List<Folder>
)
