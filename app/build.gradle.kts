plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    compileSdkVersion(28)
    dataBinding.isEnabled = true
    defaultConfig {
        applicationId = "org.gmetais.downloadmanager"
        minSdkVersion(15)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true

        val url : String by project
        val username : String by project
        val secret : String by project
        buildConfigField("String", "API_URL", url)
        buildConfigField("String", "API_USERNAME", username)
        buildConfigField("String", "API_SECRET", secret)
        // used by Room, to test migrations
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = mapOf(Pair("room.schemaLocation", "$projectDir/schemas"))
            }
        }
    }
    // used by Room, to test migrations
    sourceSets {
        getByName("androidTest").java.apply {
            setSrcDirs(srcDirs.plus(files("$projectDir/schemas")))
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}


dependencies {
    val ktVersion = "1.3.11"
    val ktxVersion = "1.1.0"
    implementation(project(":tools"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$ktVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$ktxVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$ktxVersion")
    //AppCompat
    implementation("androidx.appcompat:appcompat:1.0.2")
    implementation("androidx.fragment:fragment:1.0.0")
    implementation("com.google.android.material:material:1.0.0")
    implementation("androidx.vectordrawable:vectordrawable:1.0.1")
    implementation("androidx.preference:preference:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0-alpha3")
    //Arch Components
    implementation("androidx.lifecycle:lifecycle-extensions:2.0.0")
    kapt("androidx.lifecycle:lifecycle-compiler:2.0.0")
    implementation("androidx.room:room-runtime:2.1.0-alpha03")
    kapt("androidx.room:room-compiler:2.1.0-alpha03")
    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.4.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.4.0")
    //Testing
    //    androidTestimplementation("android.arch.persistence.room:testing:$rootProject.ext.archVersion"
    androidTestImplementation("androidx.test.espresso:espresso-core:3.1.0-beta02") {
        exclude(group = "com.android.support", module = "support-annotations")
    }
    testImplementation("junit:junit:4.12")
}

