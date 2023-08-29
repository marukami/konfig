package dev.tilbrook.konfig

import dev.tilbrook.konfig.internal.BuildConfigExtensionImpl
import dev.tilbrook.konfig.internal.FieldData
import dev.tilbrook.konfig.internal.Generator
import org.gradle.api.DefaultTask
import org.gradle.api.Generated
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.mapProperty
import org.gradle.kotlin.dsl.newInstance
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

interface BuildConfigExtension {
    fun field(name: String, value: Any)
    fun systemField(name: String, key: String, default: Any)
    fun Project.propertyField(name: String, key: String, default: Any)
    fun field(vararg pairs: Pair<String, Any>)
}

abstract class KonfigTask @Inject constructor(objectFactory: ObjectFactory) : DefaultTask() {

    @get:Input
    val packageName = objectFactory.property<String>().convention("")

    @get:Input
    val entries: MapProperty<String, List<FieldData>> =
        objectFactory.mapProperty<String, List<FieldData>>().convention(mutableMapOf())

    @get:OutputDirectory
    val destination: DirectoryProperty = objectFactory.directoryProperty()



    @TaskAction
    fun action() {
        Generator.writeConfig(
            packageName.get(),
            entries.get(),
            destination.file("generated/source/konfig").get()
        )
    }




}