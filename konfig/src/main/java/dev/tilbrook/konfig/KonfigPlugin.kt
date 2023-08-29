package dev.tilbrook.konfig

import dev.tilbrook.konfig.internal.BuildConfigExtensionImpl
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

class KonfigPlugin : Plugin<Project> {
  override fun apply(target: Project) {

    val ext: BuildConfigExtensionImpl = target.extensions.create("konfig")

    target.tasks.register<KonfigTask>("konfig") {
      packageName.set(ext.packageName)
      entries.set(ext.entries)
      destination.set(target.layout.buildDirectory)
    }
  }
}