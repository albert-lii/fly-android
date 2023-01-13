import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 依赖管理
 */
class Dep : Plugin<Project> {

    override fun apply(target: Project) {

    }

    companion object {
        /**
         * Kotlin
         */
        const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${DepVersion.kotlin}"
        const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${DepVersion.kotlin}"
        const val kotlinCoroutinesCore =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:${DepVersion.kotlinxCoroutines}"
        const val kotlinCoroutinesAndroid =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${DepVersion.kotlinxCoroutines}"

        /**
         * LifeCycle
         */
        const val lifecycleRuntime =
            "androidx.lifecycle:lifecycle-runtime-ktx:${DepVersion.lifecycle}" // LifecycleScope
        const val lifecycleCompiler =
            "androidx.lifecycle:lifecycle-compiler:${DepVersion.lifecycle}" // Lifecycle注解
        const val lifecycleCommonJava8 =
            "androidx.lifecycle:lifecycle-common-java8:${DepVersion.lifecycle}"
        const val lifecycleViewModel =
            "androidx.lifecycle:lifecycle-viewmodel-ktx:${DepVersion.lifecycle}" // ViewModelScope
        const val lifecycleLiveData =
            "androidx.lifecycle:lifecycle-livedata-ktx:${DepVersion.lifecycle}" // LiveData

        // 在2.2.0之后不再更新，被废弃
        const val lifecycleExtensions =
            "androidx.lifecycle:lifecycle-extensions:${DepVersion.lifecycleExt}"

        /**
         * IOC
         */
        const val hilt = "com.google.dagger:hilt-android:${DepVersion.hilt}"
        const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${DepVersion.hilt}"

        /**
         * Http
         */
        const val retrofit = "com.squareup.retrofit2:retrofit:${DepVersion.retrofit}"
        const val retrofitGson = "com.squareup.retrofit2:converter-gson:${DepVersion.retrofit}"
        const val retrofitScalars =
            "com.squareup.retrofit2:converter-scalars:${DepVersion.retrofit}"
        const val retrofitRxjava = "com.squareup.retrofit2:adapter-rxjava:${DepVersion.retrofit}"
        const val okhttp = "com.squareup.okhttp3:okhttp:${DepVersion.okhttp}"

        /**
         * RxJava
         */
        const val rxJava = "io.reactivex.rxjava2:rxjava:${DepVersion.rxjava}"
        const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:${DepVersion.rxjava}"
        const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${DepVersion.rxjava}"

        /**
         * Image
         */
        const val glide = "com.github.bumptech.glide:glide:${DepVersion.glide}"
        const val glideCompiler = "com.github.bumptech.glide:compiler:${DepVersion.glide}"
        const val glideOkhttpIntegration =
            "com.github.bumptech.glide:okhttp3-integration:${DepVersion.glideOkhttpIntegration}"
        const val androidsvg = "com.caverock:androidsvg-aar:${DepVersion.androidsvg}"

        /**
         * Cache
         */
        const val mmkv = "com.tencent:mmkv-static:${DepVersion.mmkv}"

        /**
         * Widget
         */
        const val appcompat = "androidx.appcompat:appcompat:${DepVersion.appcompat}"
        const val material = "com.google.android.material:material:${DepVersion.material}"
        const val recyclerview = "androidx.recyclerview:recyclerview:${DepVersion.recyclerview}"
        const val constraintlayout =
            "androidx.constraintlayout:constraintlayout:${DepVersion.constraintlayout}"
        const val coordinatorlayout =
            "androidx.coordinatorlayout:coordinatorlayout:${DepVersion.coordinatorlayout}"
        const val flexbox = "com.google.android:flexbox:${DepVersion.flexbox}"

        /**
         * Monitor
         */
        const val firebaseBom = "com.google.firebase:firebase-bom:${DepVersion.firebaseBom}"
        const val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics-ktx"
        const val firebaseAnalytics = "com.google.firebase:firebase-analytics-ktx"
        const val firebasePerf = "com.google.firebase:firebase-perf-ktx"
        const val sensorsAnalytics =
            "com.sensorsdata.analytics.android:SensorsAnalyticsSDK:${DepVersion.sensorsAnalytics}"

        /**
         * Test
         */
        const val junit = "junit:junit:${DepVersion.junit}"
        const val junitAndroid = "androidx.test.ext:junit:${DepVersion.junitAndroid}"
        const val espressoCore =
            "androidx.test.espresso:espresso-core:${DepVersion.espressoCore}"
        const val mockk = "io.mockk:mockk:${DepVersion.mockk}"
        const val coroutines_test =
            "org.jetbrains.kotlinx:kotlinx-coroutines-test:${DepVersion.coroutines_test}"
        const val robolectric = "org.robolectric:robolectric:${DepVersion.robolectric}"

        /**
         * Other
         */
        const val coreKtx = "androidx.core:core-ktx:${DepVersion.coreKtx}"
        const val activityKtx = "androidx.activity:activity-ktx:${DepVersion.activityKtx}"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:${DepVersion.fragmentKtx}"
        const val multidex = "androidx.multidex:multidex:${DepVersion.multidex}"
        const val localbroadcastmanager =
            "androidx.localbroadcastmanager:localbroadcastmanager:${DepVersion.localbroadcastmanager}"
        const val relinker = "com.getkeepsafe.relinker:relinker:${DepVersion.relinker}"
        const val arouter = "com.alibaba:arouter-api:${DepVersion.arouter}"
        const val arouterCompiler = "com.alibaba:arouter-compiler:${DepVersion.arouterCompiler}"
        const val gson = "com.google.code.gson:gson:${DepVersion.gson}"
        const val biometric = "androidx.biometric:biometric-ktx:${DepVersion.biometric}"
        const val zxing = "com.google.zxing:core:${DepVersion.zxing}"
    }
}