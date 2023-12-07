plugins {
    id("java-library")
}

project.group = "love.polardivision"
project.version = property("version")!!

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
    compileTestJava {
        options.encoding = "UTF-8"
    }
    javadoc {
        options.encoding = "UTF-8"
    }
}

java.toolchain.languageVersion = JavaLanguageVersion.of(17)

val lwjglVersion = property("lwjglVersion")
val jomlVersion = property("jomlVersion")

val lwjglNatives = Pair(
        System.getProperty("os.name")!!,
        System.getProperty("os.arch")!!
).let { (name, arch) ->
    when {
        arrayOf("Linux", "FreeBSD", "SunOS", "Unit").any { name.startsWith(it) } ->
            if (arrayOf("arm", "aarch64").any { arch.startsWith(it) })
                "natives-linux${if (arch.contains("64") || arch.startsWith("armv8")) "-arm64" else "-arm32"}"
            else
                "natives-linux"

        arrayOf("Mac OS X", "Darwin").any { name.startsWith(it) } ->
            "natives-macos${if (arch.startsWith("aarch64")) "-arm64" else ""}"

        arrayOf("Windows").any { name.startsWith(it) } ->
            if (arch.contains("64"))
                "natives-windows${if (arch.startsWith("aarch64")) "-arm64" else ""}"
            else
                "natives-windows-x86"

        else -> throw Error("Unrecognized or unsupported platform. Please set \"lwjglNatives\" manually")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    api(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    api("org.lwjgl:lwjgl")
    api("org.lwjgl:lwjgl-assimp")
    api("org.lwjgl:lwjgl-glfw")
    api("org.lwjgl:lwjgl-nfd")
    api("org.lwjgl:lwjgl-nuklear")
    api("org.lwjgl:lwjgl-openal")
    api("org.lwjgl:lwjgl-opengl")
    api("org.lwjgl:lwjgl-opus")
    api("org.lwjgl:lwjgl-stb")
    api("org.lwjgl:lwjgl-vulkan")
    api("org.lwjgl:lwjgl-yoga")

    api("org.joml:joml:${jomlVersion}")

    runtimeOnly("org.lwjgl:lwjgl::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-assimp::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-glfw::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-nfd::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-nuklear::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-openal::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-opengl::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-opus::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-stb::$lwjglNatives")
    if (lwjglNatives == "natives-macos" || lwjglNatives == "natives-macos-arm64")
        runtimeOnly("org.lwjgl:lwjgl-vulkan::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-yoga::$lwjglNatives")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
}
