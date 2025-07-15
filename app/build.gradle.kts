import com.github.triplet.gradle.androidpublisher.ReleaseStatus

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    id("com.github.triplet.play") version "3.12.1"
    id("com.gladed.androidgitversion")
}


val SELF_PLAY_UPLOAD_STORE_FILE = (rootProject.extra["SELF_PLAY_UPLOAD_STORE_FILE"] ?: System.getenv("SELF_PLAY_UPLOAD_STORE_FILE")).toString()
val SELF_PLAY_UPLOAD_STORE_PASSWORD = (rootProject.extra["SELF_PLAY_UPLOAD_STORE_PASSWORD"] ?: System.getenv("SELF_PLAY_UPLOAD_STORE_PASSWORD")).toString()
val SELF_PLAY_UPLOAD_KEY_ALIAS = (rootProject.extra["SELF_PLAY_UPLOAD_KEY_ALIAS"] ?: System.getenv("SELF_PLAY_UPLOAD_KEY_ALIAS")).toString()
val SELF_PLAY_UPLOAD_KEY_PASSWORD = (rootProject.extra["SELF_PLAY_UPLOAD_KEY_PASSWORD"] ?: System.getenv("SELF_PLAY_UPLOAD_KEY_PASSWORD")).toString()
val GOOGLE_PLAY_CREDS = (rootProject.extra["GOOGLE_PLAY_CREDS"] ?: System.getenv("GOOGLE_PLAY_CREDS")).toString()

androidGitVersion {
    codeFormat = "MNNNPPRRR"
    isUntrackedIsDirty = false
    untrackedIsDirty = false
    format = "%tag%%-count%%-commit%%-branch%"
}

android {
    namespace = "com.joinself.app.demo"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.joinself.app.demo"
        minSdk = 28
        targetSdk = 35
        versionCode = androidGitVersion.code()
        versionName = androidGitVersion.name()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        ndk {
            abiFilters.clear()
            abiFilters.add("arm64-v8a")
        }
        setProperty("archivesBaseName", "self-demo-${versionName}")
    }
    signingConfigs {
        create("release") {
            storeFile = file(SELF_PLAY_UPLOAD_STORE_FILE)
            storePassword = SELF_PLAY_UPLOAD_STORE_PASSWORD
            keyAlias = SELF_PLAY_UPLOAD_KEY_ALIAS
            keyPassword = SELF_PLAY_UPLOAD_KEY_PASSWORD
        }
    }
    buildTypes {
        debug {

        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        jniLibs {
            pickFirsts.addAll(listOf("lib/x86/libc++_shared.so", "lib/x86_64/libc++_shared.so", "lib/armeabi-v7a/libc++_shared.so", "lib/arm64-v8a/libc++_shared.so"))
            useLegacyPackaging = true
        }
        resources {
            excludes.addAll(listOf("META-INF/NOTICE", "META-INF/LICENSE", "META-INF/DEPENDENCIES", "META-INF/versions/9/OSGI-INF/MANIFEST.MF"))
        }
        dex {
            useLegacyPackaging = true
        }
    }
    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
}

dependencies {
    implementation(libs.self.sdk.android)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.compose.navigation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

play {
    serviceAccountCredentials.set(file(GOOGLE_PLAY_CREDS))
    releaseStatus.set(ReleaseStatus.DRAFT)
}
