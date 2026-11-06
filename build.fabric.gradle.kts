@file:Suppress("UnstableApiUsage")

plugins {
    id("dev.kikugie.loom-back-compat")
    id("me.modmuss50.mod-publish-plugin")
    id("maven-publish")
}

val minecraft = stonecutter.current.version
val isLegacy = stonecutter.eval(minecraft, "<26.1")
val javaVersion = property("deps.java_version") as String
val modId = property("mod.id") as String

val overlays = mapOf(
    "1.21.1" to "<26.1",
    "26.x" to ">=26.1",
    "pre-26.3" to "<26.3",
    "26.3+" to ">=26.3",
)
val activeOverlays = overlays.filter { stonecutter.eval(minecraft, it.value) }.keys
sourceSets.main {
    activeOverlays.forEach { resources.srcDir(rootProject.file("src/main/overlays/$it")) }
}

tasks.named<ProcessResources>("processResources") {
    fun prop(name: String) = project.property(name) as String

    val props = HashMap<String, String>().apply {
        this["version"] = prop("mod.version") + "+" + prop("deps.minecraft")
        this["minecraft"] = prop("mod.mc_dep_fabric")
        this["mod_id"] = prop("mod.id")
        this["mod_name"] = prop("mod.name")
        this["description"] = prop("mod.description")
        this["mod_author"] = prop("mod.author")
        this["credits"] = prop("mod.credits")
        this["license"] = prop("mod.license")
        this["fabric_loader_version"] = prop("deps.fabric_loader")
        this["java_version"] = prop("deps.java_version")
    }
    inputs.properties(props)

    filesMatching(listOf("fabric.mod.json", "*.mixins.json")) {
        expand(props)
    }
}

tasks.named("processResources") {
    dependsOn(":${stonecutter.current.project}:stonecutterGenerate")
}

version = "${property("mod.version")}+${property("deps.minecraft")}-fabric"
base.archivesName = modId

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        name = "Terraformers (Mod Menu, EMI)"
        url = uri("https://maven.terraformersmc.com/releases/")
        content {
            includeGroupAndSubgroups("com.terraformersmc")
            includeGroup("dev.emi")
        }
    }
    maven {
        name = "shedaniel (Cloth Config)"
        url = uri("https://maven.shedaniel.me/")
        content {
            includeGroupAndSubgroups("me.shedaniel")
        }
    }
    maven {
        name = "Modrinth"
        url = uri("https://api.modrinth.com/maven")
        content {
            includeGroupAndSubgroups("maven.modrinth")
        }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${property("deps.minecraft")}")
    loomx.applyMojangMappings()
    modImplementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("deps.fabric_api")}")

    // Mod Menu
    modImplementation("com.terraformersmc:modmenu:${property("deps.modmenu")}")

    // Jade
    modImplementation("maven.modrinth:jade:${property("deps.jade")}")

    if (isLegacy) {
        // Cloth Config
        modImplementation("me.shedaniel.cloth:cloth-config-fabric:${property("deps.cloth_config")}") {
            exclude(group = "net.fabricmc.fabric-api")
        }

        // EMI
        modRuntimeOnly("dev.emi:emi-fabric:${property("deps.emi")}")
    } else {
        // Cloth Config
        modImplementation("maven.modrinth:cloth-config:${property("deps.cloth_config")}")
    }
}

val accessWidener = activeOverlays.map { rootProject.file("src/main/overlays/$it/$modId.accesswidener") }.firstOrNull { it.exists() }
if (accessWidener != null) loom {
    accessWidenerPath = accessWidener
}

loom {
    runs.configureEach {
        ideConfigGenerated(true)
        runDir = "run/$name"
    }
}


tasks {
    processResources {
        exclude("**/neoforge.mods.toml", "**/mods.toml", "**/accesstransformer.cfg", "data/neoforge/**")
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        from(loomx.modJar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
        dependsOn("build")
    }
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(javaVersion)
    sourceCompatibility = JavaVersion.toVersion(javaVersion)
    targetCompatibility = JavaVersion.toVersion(javaVersion)
    withSourcesJar()
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release = javaVersion.toInt()
    options.compilerArgs.addAll(listOf("-Xmaxerrs", "5000"))
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = property("mod.group") as String
            artifactId = "$modId-fabric"
            version = "${property("mod.version")}+${property("deps.minecraft")}"

            from(components["java"])
        }
    }
}

val additionalVersions: List<String> = (findProperty("publish.additionalVersions") as String?)
    ?.split(",")
    ?.map { it.trim() }
    ?.filter { it.isNotEmpty() }
    ?: emptyList()

publishMods {
    file = loomx.modJar.map { it.archiveFile.get() }
    additionalFiles.from(loomx.modSourcesJar.map { it.archiveFile.get() })

    type = STABLE
    displayName = "${property("mod.name")} Fabric ${stonecutter.current.project.substringBeforeLast('-')} - ${property("mod.version")}"
    version = "${property("mod.version")}+${property("deps.minecraft")}-fabric"
    changelog = provider { rootProject.file("CHANGELOG-LATEST.md").readText() }
    modLoaders.add("fabric")

    modrinth {
        projectId = property("publish.modrinth") as String
        accessToken = providers.environmentVariable("MODRINTH_TOKEN").orElse(providers.environmentVariable("MODRINTH_API_KEY"))
        minecraftVersions.add(property("deps.minecraft") as String)
        minecraftVersions.addAll(additionalVersions)
        requires("fabric-api")
        optional("cloth-config")
        optional("jade")
        optional("modmenu")
    }

    curseforge {
        projectId = property("publish.curseforge") as String
        accessToken = providers.environmentVariable("CURSEFORGE_TOKEN").orElse(providers.environmentVariable("CURSEFORGE_API_KEY"))
        minecraftVersions.add(property("deps.minecraft") as String)
        minecraftVersions.addAll(additionalVersions)
        requires("fabric-api")
        optional("cloth-config")
        optional("jade")
        optional("modmenu")
        client = true
        server = true
    }
}
