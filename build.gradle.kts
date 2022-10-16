// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val composeUiVersion by extra { "1.1.1" }
}
plugins {
    val kotlinVersion = "1.6.10"
    id("com.android.application") version "7.3.1" apply false
    id("com.android.library") version "7.3.1" apply false
    id("org.jetbrains.kotlin.android") version kotlinVersion apply false
    id("org.jetbrains.kotlin.plugin.serialization") version kotlinVersion apply false
}