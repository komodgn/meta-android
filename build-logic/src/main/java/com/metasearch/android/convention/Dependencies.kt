package com.metasearch.android.convention

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

fun DependencyHandler.implementation(dependencyNotation: Any): Dependency? {
    return add("implementation", dependencyNotation)
}

fun DependencyHandler.api(dependencyNotation: Any): Dependency? {
    return add("api", dependencyNotation)
}

fun DependencyHandler.ksp(dependencyNotation: Any): Dependency? {
    return add("ksp", dependencyNotation)
}

fun DependencyHandler.detektPlugins(dependencyNotation: Any): Dependency? {
    return add("detektPlugins", dependencyNotation)
}

fun DependencyHandler.debugImplementation(dependencyNotation: Any): Dependency? {
    return add("debugImplementation", dependencyNotation)
}

fun DependencyHandler.implementationProject(path: String): Dependency? {
    return add("implementation", project(path))
}

fun DependencyHandler.testImplementation(dependencyNotation: Any): Dependency? {
    return add("testImplementation", dependencyNotation)
}

fun DependencyHandler.androidTestImplementation(dependencyNotation: Any): Dependency? {
    return add("androidTestImplementation", dependencyNotation)
}

fun DependencyHandler.testImplementationProject(path: String): Dependency? {
    return add("testImplementation", project(path))
}

fun DependencyHandler.androidTestImplementationProject(path: String): Dependency? {
    return add("androidTestImplementation", project(path))
}
