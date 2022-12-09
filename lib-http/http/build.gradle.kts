
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
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
    implementation(Dep.lifecycleRuntime)
    implementation(Dep.lifecycleCommonJava8)
    implementation(Dep.retrofit)
    implementation(Dep.retrofitGson)
    implementation(Dep.okhttp)
    implementation(Dep.mmkv)
    implementation(Dep.relinker)
}