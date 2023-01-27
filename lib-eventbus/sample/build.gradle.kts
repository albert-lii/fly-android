plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = DepVersion.compileSdk

    defaultConfig {
        applicationId = "org.fly.eventbus.sample"
        minSdk = DepVersion.minSdk
        targetSdk = DepVersion.targetSdk
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
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
    implementation(Dep.appcompat)
    implementation(Dep.material)
    implementation(project(":lib-eventbus:eventbus"))
    implementation(project(":lib-screenadapter"))
}