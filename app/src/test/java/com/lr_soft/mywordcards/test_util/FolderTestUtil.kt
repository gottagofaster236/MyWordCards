package com.lr_soft.mywordcards.test_util

import com.lr_soft.mywordcards.model.*

object FolderTestUtil {
    val grandchild =
        Folder(name = "3", emptyList(), emptyList(), null, emptyList())
    val child =
        Folder(name = "2", emptyList(), listOf(grandchild), null, emptyList())
    val anotherChild =
        Folder(name = "2.1", emptyList(), emptyList(), null, emptyList())
    val root =
        Folder(name = "1", emptyList(), listOf(child, anotherChild), null, emptyList())
    val rootPath = FolderPath(root)
    val childPath = FolderPath(listOf(root, child))
    val grandchildPath = FolderPath(listOf(root, child, grandchild))

    val updatedChild =
        Folder(name = "2 new", emptyList(), emptyList(), null, emptyList())
    val updatedRoot =
        Folder(name = "1", emptyList(), listOf(updatedChild, anotherChild), null, emptyList())
    val updatedChildPath = FolderPath(listOf(updatedRoot, updatedChild))
    val updatedRootPath = FolderPath(updatedRoot)

    val englishGermanWordPairs = listOf(
        WordPair(Word("hello", Language.EN), Word("hallo", Language.DE)),
        WordPair(Word("goodbye", Language.EN), Word("Auf Wiedersehen", Language.DE))
    )
    val englishFrenchWordPairs = listOf(
        WordPair(Word("hello", Language.EN), Word("bonjour", Language.FR)),
        WordPair(Word("goodbye", Language.EN), Word("au revoir", Language.FR))
    )
}
