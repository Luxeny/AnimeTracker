package com.example.architecture

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.ext.list.withPackage
import com.lemonappdev.konsist.api.verify.assertFalse
import com.lemonappdev.konsist.api.verify.assertTrue
import java.io.File
import org.junit.Assert.assertTrue as junitAssertTrue
import org.junit.Test

class ArchitectureTest {

  @Test
  fun `domain layer does not depend on android framework`() {
    Konsist.scopeFromProject()
      .files
      .withPackage("com.example.domain..")
      .assertFalse { file ->
        file.imports.any { importDeclaration ->
          importDeclaration.name.startsWith("android.") || importDeclaration.name.startsWith("androidx.")
        }
      }
  }

  @Test
  fun `data layer does not depend on ui components`() {
    Konsist.scopeFromProject()
      .files
      .withPackage("com.example.data..")
      .assertFalse { file ->
        file.imports.any { importDeclaration ->
          importDeclaration.name.startsWith("com.example.ui") ||
            importDeclaration.name.startsWith("androidx.compose")
        }
      }
  }

  @Test
  fun `feature ui modules do not depend on data layer`() {
    Konsist.scopeFromProject()
      .files
      .filter { file -> file.path.contains("feature") && file.path.contains("ui") }
      .assertFalse { file ->
        file.imports.any { importDeclaration ->
          importDeclaration.name.startsWith("com.example.data")
        }
      }
  }

  @Test
  fun `ui layer does not depend on data layer`() {
    val uiPackages = listOf("com.example.ui..", "com.example.feature..")
    uiPackages.forEach { uiPackage ->
      Konsist.scopeFromProject()
        .files
        .withPackage(uiPackage)
        .assertFalse { file ->
          file.imports.any { importDeclaration ->
            importDeclaration.name.startsWith("com.example.data")
          }
        }
    }
  }

  @Test
  fun `use cases reside in domain layer`() {
    Konsist.scopeFromProject()
      .classes()
      .withNameEndingWith("UseCase")
      .assertTrue { useCase -> useCase.resideInPackage("com.example.domain.usecase..") }
  }

  @Test
  fun `repository interfaces are declared in domain layer`() {
    Konsist.scopeFromProject()
      .interfaces()
      .withNameEndingWith("Repository")
      .assertTrue { repository -> repository.resideInPackage("com.example.domain.repository..") }
  }

  @Test
  fun `repository implementations reside in data layer`() {
    Konsist.scopeFromProject()
      .classes()
      .withNameEndingWith("RepositoryImpl")
      .assertTrue { repositoryImpl -> repositoryImpl.resideInPackage("com.example.data.repository..") }
  }

  @Test
  fun `feature modules do not depend on each other directly`() {
    val settingsFile = File("settings.gradle.kts")
    if (!settingsFile.exists()) return

    val featureModules = Regex("""include\("(:[^"]*feature[^"]*)"\)""")
      .findAll(settingsFile.readText())
      .map { match -> match.groupValues[1] }
      .toList()

    if (featureModules.size < 2) {
      return
    }

    featureModules.forEach { modulePath ->
      val gradleRelativePath = modulePath.removePrefix(":").replace(":", "/") + "/build.gradle.kts"
      val gradleFile = File(gradleRelativePath)
      if (!gradleFile.exists()) return@forEach

      val gradleContent = gradleFile.readText()
      val directFeatureDependencies = featureModules
        .filter { otherModule -> otherModule != modulePath }
        .filter { otherModule ->
          gradleContent.contains("project(\"$otherModule\")") ||
            gradleContent.contains("project('$otherModule')")
        }

      junitAssertTrue(
        "Module $modulePath must not depend directly on other feature modules: $directFeatureDependencies",
        directFeatureDependencies.isEmpty()
      )
    }
  }
}
