plugins {
    alias(libs.plugins.metasearch.jvm.library)
}

dependencies {
    implementation(libs.kotlinx.collections.immutable)

    compileOnly(
        libs.compose.stable.marker,
    )
}
