import java.util.Properties

plugins {
  alias(libs.plugins.android.library)
}

android {
  namespace = "com.example.core.analytics"
  compileSdk = 36
  defaultConfig {
    minSdk = 24
    
    val properties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        properties.load(localPropertiesFile.inputStream())
    }
    
    val apiKey = properties.getProperty("APPMETRICA_API_KEY") ?: ""
    buildConfigField("String", "APPMETRICA_API_KEY", "\"$apiKey\"")
  }
  buildFeatures {
    buildConfig = true
  }
}

dependencies {
  implementation(project(":core"))
  implementation(libs.appmetrica.analytics)
  implementation(libs.koin.android)
}
