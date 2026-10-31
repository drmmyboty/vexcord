plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "gg.vexcord.manager"
    compileSdk = 35

    defaultConfig {
        applicationId = "gg.vexcord.manager"
        minSdk = 23
        targetSdk = 28
        versionCode = 1
        versionName = "0.1.0"
    }
}
