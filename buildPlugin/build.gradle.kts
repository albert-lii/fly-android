plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation("com.android.tools.build:gradle:7.1.3")
    implementation("com.google.code.gson:gson:2.8.6")
}

gradlePlugin {
    plugins.register("dep") {
        // 自定义plugin的id，其他module引用要用到
        id = "org.fly.dep"
        // 自定义plugin的路径
        implementationClass = "Dep"
    }
}