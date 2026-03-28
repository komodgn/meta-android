plugins {
    alias(libs.plugins.metasearch.jvm.library)
}

dependencies {
    api(libs.kotlinx.collections.immutable)

    compileOnly(
        libs.compose.stable.marker,
    )
}
