plugins {
    alias(libs.plugins.metasearch.jvm.library)
}

dependencies {
    api(projects.data.domain)

    api(libs.androidx.paging.common)
}
