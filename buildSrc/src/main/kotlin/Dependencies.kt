@file:Suppress("ClassName", "ConstPropertyName")

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.json.JsonMapper
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.kotlin.dsl.exclude
import kotlin.reflect.KProperty

object versions {
    const val minecraft = "1.20.1"
}

object deps {
    const val minecraft = "com.mojang:minecraft:${versions.minecraft}"
    const val mixinAp = "net.fabricmc:sponge-mixin:0.12.5+mixin.0.8.5"

    object fabric : DependencyPath {
        override val prefix = "fabric"

        const val loader = "net.fabricmc:fabric-loader:0.15.10"

        object wthit : DependencyPath {
            override val prefix = "fabric.wthit"

            val api by json()
            val runtime by json()
        }

        val badpackets by json()

        val ae2 by json()
        val alloyForge by json()
        val architectury by json()
        val create by json()
        val clothConfig by json()
        val fabricApi by json()
        val flk by json()
        val kibe by json()
        val modernDynamics by json()
        val modmenu by json()
        val noIndium by json()
        val owo by json()
        val patchouli by json()
        val powah by json()

        val dml by json()
        val indrev by json()
        val luggage by json()
        val pal by json()
        val rebornCore by json()
        val techReborn by json()
        val wirelessNet by json()

        const val trEnergy = "teamreborn:energy:2.2.0"
        const val libgui = "io.github.cottonmc:LibGui:8.1.1+1.20.1"
        const val magna = "dev.draylar:magna:1.10.0+1.20.1"
        const val stepAttr = "com.github.emilyploszaj:step-height-entity-attribute:v1.2.0"
    }

    object forge : DependencyPath {
        override val prefix = "forge"

        const val forge = "net.neoforged:forge:${versions.minecraft}-47.1.97"

        object wthit : DependencyPath {
            override val prefix = "forge.wthit"

            val api by json()
            val runtime by json()
        }

        val badpackets by json()

        val ae2 by json()
        val create by json()
        val ie by json()
        val rs by json()
        val jei by json()

        object mekanism : DependencyPath {
            override val prefix = "forge.mekanism"

            val core by json()
            val generators by json()
        }

        object thermal : DependencyPath {
            override val prefix = "forge.thermal"

            val cofhCore by json()
            val foundation by json()
            val expansion by json()
        }
    }
}

fun ExternalModuleDependency.exclude(dep: String) {
    val (group, module) = dep.split(':', limit = 2)
    exclude(group, module)
}

fun Project.initializeDependencies() {
    dependenciesJson = JsonMapper().readTree(file("dependencies.json"))
}

private lateinit var dependenciesJson: JsonNode

interface DependencyPath {
    val prefix: String

    fun json() = DependencyDelegate()
}

class DependencyDelegate {
    operator fun getValue(self: DependencyPath, property: KProperty<*>): String {
        return dependenciesJson["${self.prefix}.${property.name}"].asText()
    }
}
