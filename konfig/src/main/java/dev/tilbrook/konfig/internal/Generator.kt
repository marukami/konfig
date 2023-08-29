package dev.tilbrook.konfig.internal

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.MapProperty
import javax.inject.Inject
import kotlin.reflect.KClass

object Generator {

  fun writeConfig(packageName: String, entries: Map<String, List<FieldData>>, file: RegularFile) {
    // TODO Use named flavours
    val props = entries[""]!!.map {
        PropertySpec
          .builder(it.name, it.type.toKClass(), KModifier.CONST)
          .initializer(it.value)
          .build()
    }

    val objectSpec = FileSpec
      .builder(packageName, "BuildKonfig")
      .addType(
        TypeSpec
          .objectBuilder("BuildKonfig")
          .addProperties(props)
          .build()
      )
      .build()

    objectSpec.writeTo(file.asFile)
  }
}

fun ConfigType.toKClass(): KClass<*> = when(this) {
  ConfigType.String -> String::class
  ConfigType.Boolean -> Boolean::class
  ConfigType.Char -> Char::class
  ConfigType.Byte -> Byte::class
  ConfigType.Short -> Short::class
  ConfigType.Int -> Int::class
  ConfigType.Float -> Float::class
  ConfigType.Long -> Long::class
  ConfigType.Double -> Double::class
}