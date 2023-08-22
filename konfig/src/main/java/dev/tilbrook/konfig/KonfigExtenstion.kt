package dev.tilbrook.konfig

import org.gradle.api.Project

interface BuildConfigExtension {
    fun field(name: String, value: Any)
    fun systemField(name: String, key: String, default: Any)
    fun Project.propertyField(name: String, key: String, default: Any)
    fun field(vararg pairs: Pair<String, Any>)
    fun field(name: String, value: Any, type: String)
}