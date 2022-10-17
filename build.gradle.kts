// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val composeUiVersion by extra { "1.1.1" }
    val kotlinVersion by extra { "1.6.10" }
    val hiltVersion by extra { "2.44" }
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