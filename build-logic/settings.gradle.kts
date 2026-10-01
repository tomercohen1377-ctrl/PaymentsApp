dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
    // Share the app's version catalog so plugin and library versions are defined once.
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"
include(":convention")
