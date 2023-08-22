package dev.tilbrook.konfig.internal

import dev.tilbrook.konfig.BuildConfigExtension
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.kotlin.dsl.listProperty
import javax.inject.Inject

data class ConfigEntry(
  val name: String,
  val value: Any,
  val type: String,
)

open class BuildConfigExtensionImpl @Inject constructor(
  objectFactory: ObjectFactory
) : BuildConfigExtension {

  internal val entries: ListProperty<ConfigEntry> =
    objectFactory.listProperty<ConfigEntry>().convention(mutableListOf())

  override fun field(name: String, value: Any) {
    val typeName = requireNotNull(value.toPrimativeTypeName()) {
      """
       Only primitives are supported, if need a complex type then use 
       field(name: String, value: Any, type: String) 
      """.trimIndent()
    }
    field(name, value, typeName)
  }

  override fun field(vararg pairs: Pair<String, Any>) {
    pairs.forEach { (name, value) ->
      field(name, value)
    }
  }

  override fun field(name: String, value: Any, type: String) {
    entries.add(ConfigEntry(name, value, type))
  }

  override fun systemField(name: String, key: String, default: Any) {
    val value = System.getenv().getOrDefault(key, default)
    field(name, value)
  }

  override fun Project.propertyField(name: String, key: String, default: Any) {
    val value = properties[key] ?: default
    field(name, value)
  }

  private fun Any.toPrimativeTypeName(): String? {
    return when (this) {
      is String -> {
        if (startsWith("0x")) "Int"
        else "String"
      }

      is Boolean -> "Boolean"
      is Char -> "Char"
      is Byte -> "Byte"
      is Short -> "Short"
      is Int -> "Int"
      is Float -> "Float"
      is Long -> "Long"
      is Double -> "Double"
      else -> null
    }
  }

}