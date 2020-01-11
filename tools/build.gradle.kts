
plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdkVersion(28)

    kotlinOptions {
        // We have to add the explicit cast before accessing the options itself.
        // If we don't, it does not work: "unresolved reference: jvmTarget"
        val options = this as org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
        options.jvmTarget = "1.8"
    }

    defaultConfig {
        minSdkVersion(15)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }
}

dependencies {
    api("androidx.appcompat:appcompat:1.1.0")
    api("androidx.lifecycle:lifecycle-extensions:2.1.0")
    implementation(fileTree("dir" to "libs", "include" to listOf("*.jar")))

    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.61")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.3")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0-rc03")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0-rc03")

    implementation("androidx.fragment:fragment-ktx:1.2.0-rc05")
}
