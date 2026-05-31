plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.compose)
}

android {
  namespace = "com.example.feature.auth"
  compileSdk = 36
  defaultConfig {
    minSdk = 24
  }
  buildFeatures {
    compose = true
  }
}

dependencies {
  implementation(project(":core"))
  implementation(project(":core:analytics"))
  implementation(libs.androidx.security.crypto)
  implementation(libs.yandex.auth)
  implementation(libs.vk.auth)
  implementation(libs.koin.android)
  implementation(libs.koin.androidx.compose)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
}
