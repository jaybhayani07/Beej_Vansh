plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // KSP version must match your Kotlin version (Currently 2.1.0-1.0.29 or similar)
    id("com.google.devtools.ksp") version "2.1.0-1.0.29" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
}