import java.util.Properties

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.roborazzi)
  alias(libs.plugins.secrets)
}

android {
  namespace = "com.example"
  compileSdk {
    version = release(36) {
      minorApiLevel = 1
    }
  }

  defaultConfig {
    applicationId = "com.luxeny.animetracker"
    minSdk = 24
    targetSdk = 36
    versionCode = 1
    versionName = "1.0"

    val properties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        properties.load(localPropertiesFile.inputStream())
    }

    val yandexId = properties.getProperty("YANDEX_CLIENT_ID") ?: "dummy_yandex_id"
    val vkId = properties.getProperty("VK_APP_ID") ?: "dummy_vk_id"
    val mapsKey = properties.getProperty("MAPS_API_KEY") ?: "dummy_maps_key"
    val yandexMapsKey = properties.getProperty("YANDEX_MAPS_API_KEY") ?: "dummy_yandex_maps_key"

    manifestPlaceholders["YANDEX_CLIENT_ID"] = yandexId
    manifestPlaceholders["VK_APP_ID"] = vkId
    manifestPlaceholders["MAPS_API_KEY"] = mapsKey
    
    buildConfigField("String", "YANDEX_MAPS_API_KEY", "\"$yandexMapsKey\"")

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  signingConfigs {
    create("release") {
      val keystorePath = System.getenv("KEYSTORE_PATH") ?: "${rootDir}/my-upload-key.jks"
      storeFile = file(keystorePath)
      storePassword = System.getenv("STORE_PASSWORD")
      keyAlias = "upload"
      keyPassword = System.getenv("KEY_PASSWORD")
    }
  }

  buildTypes {
    release {
      isCrunchPngs = false
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfig = signingConfigs.getByName("release")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  buildFeatures {
    compose = true
    buildConfig = true
  }
  testOptions { unitTests { isIncludeAndroidResources = true } }
}

secrets {
  propertiesFileName = ".env"
  defaultPropertiesFileName = ".env.example"
}

dependencies {
  implementation(project(":core"))
  implementation(project(":core:model"))
  implementation(project(":core:data"))
  implementation(project(":core:navigation"))
  implementation(project(":core:analytics"))
  implementation(project(":feature:auth"))
  implementation(project(":feature:about"))
  implementation(project(":feature:explore:domain"))
  implementation(project(":feature:explore:data"))
  implementation(project(":feature:explore:ui"))
  implementation(project(":feature:watchlist:domain"))
  implementation(project(":feature:watchlist:data"))
  implementation(project(":feature:watchlist:ui"))
  implementation(project(":feature:detail:domain"))
  implementation(project(":feature:detail:data"))
  implementation(project(":feature:detail:ui"))

  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.compose.material.icons.core)
  implementation(libs.androidx.compose.material.icons.extended)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.koin.android)
  implementation(libs.koin.androidx.compose)
  implementation(libs.coil.compose)
  implementation(libs.okhttp)

  testImplementation(libs.junit)
  testImplementation(libs.konsist)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.mockk)
  testImplementation(project(":core:analytics"))
  testImplementation(libs.robolectric)
  testImplementation(libs.roborazzi)
  testImplementation(libs.roborazzi.compose)
  testImplementation(libs.roborazzi.junit.rule)
  testImplementation(libs.androidx.core)
  testImplementation(libs.androidx.runner)
  testImplementation(libs.androidx.compose.ui.test.junit4)
  debugImplementation(libs.androidx.compose.ui.test.manifest)

  androidTestImplementation(libs.androidx.junit)
  debugImplementation(libs.androidx.compose.ui.tooling)
}
