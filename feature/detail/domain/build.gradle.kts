plugins {
  alias(libs.plugins.kotlin.jvm)
}

kotlin {
  jvmToolchain(11)
}

dependencies {
  implementation(project(":core"))
  implementation(project(":core:model"))
  implementation(libs.kotlinx.coroutines.core)
  implementation(libs.koin.core)
}
