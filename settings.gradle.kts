
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}


dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io" )
        maven {
            url = uri("https://maven.pkg.github.com/iZettle/sdk-android")
            credentials {
                username = "vlzdtecnopac"
                password = System.getenv("GITHUB_TOKEN")
            }
        }


    }
}



rootProject.name = "Momo Kiosko"
include(":app")
