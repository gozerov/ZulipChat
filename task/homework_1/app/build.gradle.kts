plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.navigation.safe.args)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "ru.gozerov.tfs_spring"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.gozerov.tfs_spring"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
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
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = "18"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.coil)
    implementation(libs.shimmer.android)

    implementation(libs.retrofit2)
    implementation(libs.moshi.converter)
    implementation(platform(libs.okhttp3.bom))
    implementation(libs.okhttp3)
    implementation(libs.okhttp3.logging.interceptor)

    implementation(libs.elmslie.core)
    implementation(libs.elmslie.android)
    implementation(libs.elmslie.coroutines)

    implementation(libs.dagger)
    ksp(libs.dagger.compiler)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.paging)

}