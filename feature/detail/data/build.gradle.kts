plugins {
  alias(libs.plugins.android.library)
}

android {
  namespace = "com.example.feature.detail.data"
  compileSdk {
    version = release(36) {
      minorApiLevel = 1
    }
  }
  defaultConfig { minSdk = 24 }
}

dependencies {
  api(project(":core:data"))
}
