plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}



android {
    compileSdkVersion(28)
    viewBinding.isEnabled = true

//kotlinOptions {
//    jvmTarget = "1.8"
//}
    kotlinOptions {
        // We have to add the explicit cast before accessing the options itself.
        // If we don't, it does not work: "unresolved reference: jvmTarget"
        val options = this as org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
        options.jvmTarget = "1.8"
    }

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
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
        }
    }
}

dependencies {
val ktVersion = "1.3.61"
val ktxVersion = "1.3.2"
implementation(project(":tools"))
implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$ktVersion")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$ktxVersion")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$ktxVersion")
//AppCompat
implementation("androidx.appcompat:appcompat:1.1.0")
implementation("androidx.activity:activity-ktx:1.1.0-rc03")
implementation("androidx.fragment:fragment-ktx:1.2.0-rc05")
implementation("com.google.android.material:material:1.0.0")
implementation("androidx.vectordrawable:vectordrawable:1.1.0")
implementation("androidx.preference:preference:1.1.0")
implementation("androidx.constraintlayout:constraintlayout:1.1.3")
//Arch Components
implementation("androidx.lifecycle:lifecycle-extensions:2.1.0")
kapt("androidx.lifecycle:lifecycle-compiler:2.1.0")
implementation("androidx.room:room-ktx:2.2.3")
kapt("androidx.room:room-compiler:2.2.3")
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0-rc03")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.2.0-rc03")
//Retrofit
implementation("com.squareup.retrofit2:retrofit:2.6.4")
implementation("com.squareup.retrofit2:converter-moshi:2.6.1")
//Testing
//    androidTestimplementation("android.arch.persistence.room:testing:$rootProject.ext.archVersion"
androidTestImplementation("androidx.test.espresso:espresso-core:3.1.1") {
exclude(group = "com.android.support", module = "support-annotations")
}
testImplementation("junit:junit:4.12")
}

