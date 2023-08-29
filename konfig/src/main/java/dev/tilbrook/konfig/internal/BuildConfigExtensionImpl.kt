package dev.tilbrook.konfig.internal

import dev.tilbrook.konfig.BuildConfigExtension
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.mapProperty
import org.gradle.kotlin.dsl.newInstance
import org.gradle.kotlin.dsl.property
import java.io.Serializable
import javax.inject.Inject

enum class ConfigType {
  String,
  Boolean,
  Char,
  Byte,
  Short,
  Int,
  Float,
  Long,
  Double,
}

data class FieldData(
  val name: String,
  val value: String,
  val type: ConfigType,
  // TODO Add nullable
): Serializable

internal open class BuildConfigExtensionImpl @Inject constructor(
  private val objectFactory: ObjectFactory
) : BuildConfigExtension {

  internal val flavour: Property<String> = objectFactory.property<String>().convention("")

  internal val packageName: Property<String> = objectFactory.property<String>().convention("")

  internal val entries: MapProperty<String, List<FieldData>> =
    objectFactory.mapProperty<String, List<FieldData>>().convention(mutableMapOf())

  fun flavour(name: String, action: BuildConfigExtension.() -> Unit) {
    val collector: BuildConfigExtensionImpl = objectFactory.newInstance()
    collector.flavour.set(name)
    collector.apply(action)
    entries.putAll(collector.entries)
  }

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

  override fun systemField(name: String, key: String, default: Any) {
    val value = System.getenv().getOrDefault(key, default)
    field(name, value)
  }

  override fun Project.propertyField(name: String, key: String, default: Any) {
    val value = properties[key] ?: default
    field(name, value)
  }

  private fun field(name: String, value: Any, type: ConfigType) {
    entries.put(flavour.get(), listOf(FieldData(name, value.toString(), type)))
  }

  private fun Any.toPrimativeTypeName(): ConfigType? {
    return when (this) {
      is String -> {
        if (startsWith("0x")) ConfigType.Int
        else ConfigType.String
      }

      is Boolean -> ConfigType.Boolean
      is Char -> ConfigType.Char
      is Byte -> ConfigType.Byte
      is Short -> ConfigType.Short
      is Int -> ConfigType.Int
      is Float -> ConfigType.Float
      is Long -> ConfigType.Long
      is Double -> ConfigType.Double
      else -> null
    }
  }

}