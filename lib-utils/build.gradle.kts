
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
}

android {
    compileSdk = DepVersion.compileSdk

    defaultConfig {
        minSdk = DepVersion.minSdk
        targetSdk = DepVersion.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = DepVersion.jvmTarget
    }
}

dependencies {
    testApi(Dep.junit)
    androidTestImplementation(Dep.junitAndroid)
    androidTestImplementation(Dep.espressoCore)
    implementation(Dep.kotlinStdlib)
    implementation(Dep.kotlinCoroutinesCore)
    implementation(Dep.kotlinCoroutinesAndroid)
    implementation(Dep.coreKtx)
    implementation(Dep.mmkv)
    implementation(Dep.relinker)
    implementation(Dep.lifecycleViewModel)
    implementation(Dep.hilt)
    kapt(Dep.hiltCompiler)
    implementation(Dep.recyclerview)
    implementation(Dep.gson)
}