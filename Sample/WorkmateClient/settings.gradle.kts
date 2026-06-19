pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        flatDir {
            dirs("libs")
        }
        maven {
            url = uri("https://maven.mappls.com/repository/mappls/")
        }
        maven{
            url = uri("https://maven.mappls.com/repository/workmate/")
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        flatDir {
            dirs("libs")
        }
        maven {
            url = uri("https://maven.mappls.com/repository/mappls/")
        }
        maven{
            url = uri("https://maven.mappls.com/repository/workmate/")
        }
    }
}

rootProject.name = "WorkmateClient"
include(":app")
 