// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.navigation.safe.args) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.kapt) apply false
}