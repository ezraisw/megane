import me.modmuss50.mpp.ReleaseType

plugins {
    id("fabric-loom") version "1.5.+"

    id("me.modmuss50.mod-publish-plugin")
}

publishMods {
    changelog.set("https://github.com/badasintended/megane/releases/tag/${project.version}")
    type.set(ReleaseType.STABLE)
    modLoaders.add("fabric")

    val curseForgeApi = providers.environmentVariable("CURSEFORGE_API")
    val modrinthToken = providers.environmentVariable("MODRINTH_TOKEN")
    dryRun.set(!(curseForgeApi.isPresent && modrinthToken.isPresent))

    curseforge {
        projectId.set("408118")
        accessToken.set(curseForgeApi)
        minecraftVersions.add("1.20.1")

        requires(cfSlugs.wthitFabric)
        optional(
            cfSlugs.alloyForge,
            cfSlugs.ae2,
            cfSlugs.createFabric,
            cfSlugs.dml,
//            cfSlugs.extraGen,
            cfSlugs.indrev,
            cfSlugs.kibe,
//            cfSlugs.luggage,
            cfSlugs.modernDynamics,
            cfSlugs.powah,
            cfSlugs.rebornCore,
            cfSlugs.techReborn,
            cfSlugs.wirelessNet,
        )
    }

    modrinth {
        projectId.set("ZNk5S5U6")
        accessToken.set(modrinthToken)
        minecraftVersions.add("1.20.1")

        requires(mrIds.wthit)
        optional(
            mrIds.alloyForge,
            mrIds.ae2,
            mrIds.createFabric,
            mrIds.dml,
//            mrIds.extraGen,
            mrIds.kibe,
            mrIds.modernDynamics,
            mrIds.powah,
        )
    }
}

allprojects {
    apply(plugin = "fabric-loom")

    configurations.all {
        resolutionStrategy {
            force(deps.fabric.loader)
        }
    }

    dependencies {
        minecraft(deps.minecraft)
        mappings(loom.officialMojangMappings())

        modImplementation(deps.fabric.loader)
        modImplementation(deps.fabric.fabricApi)

        modCompileOnly(deps.fabric.wthit.api)
        modRuntimeOnly(deps.fabric.wthit.runtime)
        modRuntimeOnly(deps.fabric.badpackets)
    }

    loom {
        interfaceInjection.enableDependencyInterfaceInjection.set(false)

        mixin {
            defaultRefmapName.set("megane-${project.name}.refmap.json")
        }
    }

    sourceSets {
        main {
            resources.srcDir("src/generated/resources")
        }
    }

    tasks.processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
    }
}

repositories {
    terraformers()
}

dependencies {
    modImplementation(deps.fabric.modmenu)
    modImplementation(deps.fabric.wthit.runtime)
}

loom {
    runs.configureEach {
        ideConfigGenerated(true)
    }
}

afterEvaluate {
    subprojects.forEach {
        dependencies {
            implementation(project(path = it.path, configuration = "namedElements"))

            include(project(it.path)) {
                isTransitive = false
            }
        }

        sourceSets {
            main {
                runtimeClasspath += it.sourceSets.main.get().runtimeClasspath
            }
        }
    }

    publishMods {
        file.set(tasks.remapJar.get().archiveFile)
    }
}

subprojects {
    base {
        archivesName.set("megane-fabric-${project.name}")
    }

    configurations {
        create("decompile")
    }

    loom {
        addRemapConfiguration("modDecompile") {
            targetConfigurationName.set("decompile")
            onCompileClasspath.set(true)
            onRuntimeClasspath.set(true)
        }
    }

    tasks {
        create<DecompileDependenciesTask>("decompileDependencies")
    }
}
