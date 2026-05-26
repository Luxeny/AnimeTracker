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
  fun `domain layers do not depend on android framework`() {
    Konsist.scopeFromProject()
      .files
      .filter { file -> file.path.contains("domain") && file.path.contains("feature") }
      .assertFalse { file ->
        file.imports.any { importDeclaration ->
          importDeclaration.name.startsWith("android.") || importDeclaration.name.startsWith("androidx.")
        }
      }
  }

  @Test
  fun `data layers do not depend on ui components`() {
    Konsist.scopeFromProject()
      .files
      .filter { file -> file.path.contains("data") && (file.path.contains("core") || file.path.contains("feature")) }
      .assertFalse { file ->
        file.imports.any { importDeclaration ->
          importDeclaration.name.contains(".ui") || importDeclaration.name.startsWith("androidx.compose")
        }
      }
  }

  @Test
  fun `use cases reside in feature domain layers`() {
    Konsist.scopeFromProject()
      .classes()
      .withNameEndingWith("UseCase")
      .assertTrue { useCase -> useCase.resideInPackage("..domain.usecase..") }
  }

  @Test
  fun `repository interfaces are declared in core model layer`() {
    Konsist.scopeFromProject()
      .interfaces()
      .withNameEndingWith("Repository")
      .assertTrue { repository -> repository.resideInPackage("com.example.core.model.repository..") }
  }

  @Test
  fun `repository implementations reside in core data layer`() {
    Konsist.scopeFromProject()
      .classes()
      .withNameEndingWith("RepositoryImpl")
      .assertTrue { repositoryImpl -> repositoryImpl.resideInPackage("com.example.core.data.repository..") }
  }

  @Test
  fun `feature modules do not depend on each other directly`() {
    val settingsFile = File("settings.gradle.kts")
    if (!settingsFile.exists()) return

    val featureModules = Regex("""include\("(:[^"]*feature[^"]*)"\)""")
      .findAll(settingsFile.readText())
      .map { match -> match.groupValues[1] }
      .toList()

    if (featureModules.size < 2) return

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
