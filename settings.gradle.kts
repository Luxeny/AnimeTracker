pluginManagement {
  repositories {
    google {
      content {
        includeGroupByRegex("com\\.android.*")
        includeGroupByRegex("com\\.google.*")
        includeGroupByRegex("androidx.*")
      }
    }
    mavenCentral()
    gradlePluginPortal()
  }
}

plugins { id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0" }

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
  }
}

rootProject.name = "AnimeTracker"

include(":app")
include(":core")
include(":core:model")
include(":core:data")
include(":core:navigation")
include(":feature:explore:domain")
include(":feature:explore:data")
include(":feature:explore:ui")
include(":feature:watchlist:domain")
include(":feature:watchlist:data")
include(":feature:watchlist:ui")
include(":feature:detail:domain")
include(":feature:detail:data")
include(":feature:detail:ui")
