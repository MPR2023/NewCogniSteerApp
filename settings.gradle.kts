dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()  // Include this if you're fetching plugins from the Gradle Plugin Portal
    }
}
rootProject.name = "NewCogniSteerApp"
include(":app")
 