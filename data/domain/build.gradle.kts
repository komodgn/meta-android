plugins {
    alias(libs.plugins.metasearch.jvm.library)
}

dependencies {
    compileOnly(
        libs.compose.stable.marker,
    )
}
