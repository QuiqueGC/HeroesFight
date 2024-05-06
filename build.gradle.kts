// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    //para pasar parámetros a través del navController
    id("androidx.navigation.safeargs.kotlin") version "2.7.1" apply false
    //ksp
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
}