package com.lr_soft.mywordcards.data

data class Folder(
    val name: String,
    val wordPairs: List<WordPair>,
    val subfolders: List<Folder>
)
