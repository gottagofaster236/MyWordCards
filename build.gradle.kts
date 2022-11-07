// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val composeUiVersion by extra { "1.3.2" }
    val kotlinVersion by extra { "1.7.20" }
    val hiltVersion by extra { "2.44" }
    val mockitoVersion by extra { "4.8.1" }
    val navVersion by extra { "2.5.3" }
}
plugins {
    val kotlinVersion: String by extra
    val hiltVersion: String by extra
    id("com.android.application") version "7.3.1" apply false
    id("com.android.library") version "7.3.1" apply false
    id("org.jetbrains.kotlin.android") version kotlinVersion apply false
    id("org.jetbrains.kotlin.plugin.serialization") version kotlinVersion apply false
    id("com.google.dagger.hilt.android") version hiltVersion apply false
}