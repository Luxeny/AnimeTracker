plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.compose)
}

android {
  namespace = "com.example.feature.explore.ui"
  compileSdk {
    version = release(36) {
      minorApiLevel = 1
    }
  }
  defaultConfig { minSdk = 24 }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  buildFeatures { compose = true }
}

dependencies {
  implementation(project(":core"))
  implementation(project(":core:model"))
  implementation(project(":feature:explore:domain"))
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.compose.material.icons.core)
  implementation(libs.androidx.compose.material.icons.extended)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui)
  implementation(libs.coil.compose)
}
