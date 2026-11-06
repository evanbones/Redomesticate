plugins {
    id("net.neoforged.moddev")
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
        this["minecraft_version_range"] = prop("mod.mc_dep_forgelike")
        this["mod_id"] = prop("mod.id")
        this["mod_name"] = prop("mod.name")
        this["description"] = prop("mod.description")
        this["mod_author"] = prop("mod.author")
        this["credits"] = prop("mod.credits")
        this["license"] = prop("mod.license")
        this["neoforge_loader_version_range"] = prop("deps.neoforge_loader_version_range")
        this["neoforge_version"] = prop("deps.neoforge")
        // NeoForge 26.2 deprecated logoFile (with a warning screen) in favour of iconFile/bannerFile
        this["logo_property"] = if (stonecutter.eval(minecraft, ">=26.2")) "iconFile" else "logoFile"
        this["java_version"] = prop("deps.java_version")
    }
    inputs.properties(props)

    filesMatching(listOf("META-INF/neoforge.mods.toml", "*.mixins.json")) {
        expand(props)
    }
}

version = "${property("mod.version")}+${property("deps.minecraft")}-neoforge"
base.archivesName = modId

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        name = "Terraformers (EMI)"
        url = uri("https://maven.terraformersmc.com/releases/")
        content {
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

neoForge {
    enable {
        version = property("deps.neoforge") as String
        isDisableRecompilation = true
    }
    validateAccessTransformers = true

    activeOverlays.map { rootProject.file("src/main/overlays/$it/META-INF/accesstransformer.cfg") }
        .firstOrNull { it.exists() }
        ?.let { accessTransformers.from(it.absolutePath) }

    runs {
        configureEach {
            systemProperty("neoforge.warnings.onlyin.hide", "true")
        }
        register("client") {
            gameDirectory = file("run/client")
            client()
        }
        register("server") {
            gameDirectory = file("run/server")
            server()
        }
    }

    mods {
        register(modId) {
            sourceSet(sourceSets["main"])
        }
    }
}

tasks {
    processResources {
        exclude("**/fabric.mod.json", "**/*.accesswidener", "**/mods.toml", "$modId.fabric.mixins.json")
    }

    named("createMinecraftArtifacts") {
        dependsOn("stonecutterGenerate")
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        from(jar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
        dependsOn("build")
    }
}

dependencies {
    // Jade
    implementation("maven.modrinth:jade:${property("deps.jade")}")

    if (isLegacy) {
        // Cloth Config
        implementation("me.shedaniel.cloth:cloth-config-neoforge:${property("deps.cloth_config")}")

        // EMI
        runtimeOnly("dev.emi:emi-neoforge:${property("deps.emi")}")
    } else {
        // Cloth Config
        implementation("maven.modrinth:cloth-config:${property("deps.cloth_config")}")
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
            artifactId = "$modId-neoforge"
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
    file = tasks.jar.map { it.archiveFile.get() }
    additionalFiles.from(tasks.named<org.gradle.jvm.tasks.Jar>("sourcesJar").map { it.archiveFile.get() })

    type = STABLE
    displayName = "${property("mod.name")} NeoForge ${stonecutter.current.project.substringBeforeLast('-')} - ${property("mod.version")}"
    version = "${property("mod.version")}+${property("deps.minecraft")}-neoforge"
    changelog = provider { rootProject.file("CHANGELOG-LATEST.md").readText() }
    modLoaders.add("neoforge")

    modrinth {
        projectId = property("publish.modrinth") as String
        accessToken = providers.environmentVariable("MODRINTH_TOKEN").orElse(providers.environmentVariable("MODRINTH_API_KEY"))
        minecraftVersions.add(property("deps.minecraft") as String)
        minecraftVersions.addAll(additionalVersions)
        optional("cloth-config")
        optional("jade")
    }

    curseforge {
        projectId = property("publish.curseforge") as String
        accessToken = providers.environmentVariable("CURSEFORGE_TOKEN").orElse(providers.environmentVariable("CURSEFORGE_API_KEY"))
        minecraftVersions.add(property("deps.minecraft") as String)
        minecraftVersions.addAll(additionalVersions)
        optional("cloth-config")
        optional("jade")
        client = true
        server = true
    }
}
