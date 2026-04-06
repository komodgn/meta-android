plugins {
    alias(libs.plugins.metasearch.jvm.library)
}

dependencies {
    api(projects.data.domain)
}
