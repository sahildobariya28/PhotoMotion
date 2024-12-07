plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "photo.photomotion.motion.photomotionlivewallpaper"
    compileSdk = 34

    defaultConfig {
        applicationId = "photo.photomotion.motion.photomotionlivewallpaper"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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

    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/license.txt")
        exclude("META-INF/NOTICE")
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/notice.txt")
        exclude("META-INF/ASL2.0")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity:1.9.2")
    implementation("com.google.firebase:firebase-analytics:22.1.2")
//    implementation("com.google.firebase:firebase-crashlytics:19.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("org.jetbrains:annotations:23.0.0")

    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")

    implementation("com.airbnb.android:lottie:3.5.0")
    implementation("com.github.orangegangsters:swipy:1.2.3@aar")
    implementation("com.isseiaoki:simplecropview:1.1.8")
    implementation("com.makeramen:roundedimageview:2.3.0")
    implementation("com.github.hotchemi:android-rate:1.0.1")

    implementation("com.getkeepsafe.taptargetview:taptargetview:1.14.0")
    implementation("org.apache.httpcomponents:httpclient:4.5.13")


    implementation("com.arthenica:mobile-ffmpeg-min-gpl:4.4")

    
    implementation("com.google.android.material:material:1.5.0")

    // Circle Indicator (To fix the xml preview "Missing classes" error)
    implementation("me.relex:circleindicator:2.1.6")

    implementation("org.imaginativeworld.whynotimagecarousel:whynotimagecarousel:2.1.0")

}