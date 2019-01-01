
plugins {
    id("com.android.feature")
    id("kotlin-android")
}

android {
    compileSdkVersion(28)

    baseFeature = true

    defaultConfig {
        minSdkVersion(15)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
        }
    }

}

dependencies {
    api("androidx.appcompat:appcompat:1.0.2")
    api("androidx.lifecycle:lifecycle-extensions:2.0.0")
    implementation(fileTree("dir" to "libs", "include" to listOf("*.jar")))

    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test:runner:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.1.1")
    application(project(":app"))
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.11")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.0.1")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.0.1")
}
