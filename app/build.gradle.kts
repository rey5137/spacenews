plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
}


android {
    setCompileSdkVersion(Version.TARGET_SDK)

    defaultConfig {
        applicationId = "com.rey.spacenews"
        minSdk = Version.MINIMUM_SDK
        targetSdk = Version.TARGET_SDK
        versionCode = Version.CODE
        versionName = Version.NAME
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "API_ENDPOINT", "\"https://api.spaceflightnewsapi.net/v3/\"")
    }

    buildFeatures {
        compose = true
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"))
        }
    }

    compileOptions {
        // Flag to enable support for the new language APIs
        isCoreLibraryDesugaringEnabled = true
        // Sets Java compatibility to Java 8
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.0.5"
    }
}

dependencies {

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    testImplementation("junit:junit:4.+")

    implementation(Libs.AndroidX.CORE)
    implementation(Libs.AndroidX.APP_COMPAT)
    implementation(Libs.AndroidX.MATERIAL)
    implementation(Libs.AndroidX.START_UP)
    implementation(Libs.Coroutines.CORE)
    implementation(Libs.Coroutines.ANDROID)
    implementation(Libs.Koin.CORE)
    implementation(Libs.Koin.ANDROID)

    implementation(Libs.ComposeUI.UI)
    implementation(Libs.ComposeUI.TOOLING)
    implementation(Libs.ComposeUI.FOUNDATION)
    implementation(Libs.ComposeUI.MATERIAL)
    implementation(Libs.ComposeUI.MATERIAL_ICONS)
    implementation(Libs.ComposeUI.MATERIAL_ICONS_EXTENDED)
    implementation(Libs.ComposeUI.ACTIVITY)
    implementation(Libs.Accompanist.SWIPE_REFRESH)

    implementation(Libs.Conductor.CORE)
    implementation(Libs.Common.TIMBER)

    implementation(Libs.Retrofit.CORE)
    implementation(Libs.Retrofit.MOSHI)

    implementation(Libs.Coil.CORE)
    implementation(Libs.Coil.COMPOSE)

    testImplementation(Libs.Coroutines.TEST)
}