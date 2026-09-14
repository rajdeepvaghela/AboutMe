import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.INT
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.buildkonfig)
}

val secretProps = Properties().apply {
    rootProject.file("secret.properties").takeIf { it.exists() }?.inputStream()?.use { load(it) }
}
val secretSupabaseUrl: String =
    System.getenv("SUPABASE_URL") ?: secretProps.getProperty("SUPABASE_URL", "")
val secretSupabaseKey: String =
    System.getenv("SUPABASE_KEY") ?: secretProps.getProperty("SUPABASE_KEY", "")


kotlin {

    android {
        namespace = "com.rdapps.aboutme.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
        androidResources {
            enable = true
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            freeCompilerArgs += "-Xbinary=bundleId=com.rdapps.aboutme"
        }
    }

    js {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    jvm()

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.android)
            implementation(libs.kstore.file)
            implementation(libs.koin.android)
        }
        commonMain {
            dependencies {
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)
                implementation(libs.compose.components.resources)
                implementation(libs.compose.uiToolingPreview)
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation(libs.androidx.material.icons.extended)

                // coil
                implementation(libs.coil.compose)
                implementation(libs.coil.network.ktor)

                // sketch (animated GIF on all platforms; see skiko note in README)
                implementation(libs.sketch.compose)
                implementation(libs.sketch.http)
                implementation(libs.sketch.animated.gif)

                // supabase
                implementation(project.dependencies.platform(libs.supabaseBom))
                implementation(libs.supabase.postgrest.kt)

                // serialization
                implementation(libs.kotlinx.serialization.json)

                // KStore
                implementation(libs.kstore)

                // Koin
                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)

                implementation(libs.valuepickerslider)
                implementation(libs.viewslider)
                implementation(libs.circularlist)
                implementation(libs.stepper)
            }
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.kstore.file)
        }
        jsMain.dependencies {
            implementation(libs.ktor.client.js)
            implementation(libs.kstore.storage)
        }
        wasmJsMain.dependencies {
            implementation(libs.ktor.client.wasm)
            implementation(libs.kstore.storage)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.kstore.file)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.rdapps.aboutme.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Exe, TargetFormat.Deb)
            packageName = "AboutMe"
            packageVersion = "1.0.0"
        }
    }
}

buildkonfig {
    packageName = "com.rdapps.aboutme"

    defaultConfigs {
        buildConfigField(STRING, "SUPABASE_URL", secretSupabaseUrl)
        buildConfigField(STRING, "SUPABASE_KEY", secretSupabaseKey)
        buildConfigField(BOOLEAN, "DEBUG", "true")
        // Mirrors androidApp versionCode/versionName (AGP BuildConfig is app-module only now)
        buildConfigField(STRING, "VERSION_NAME", "\"1.0.1\"")
        buildConfigField(INT, "VERSION_CODE", "2")
    }

    defaultConfigs("release") {
        buildConfigField(BOOLEAN, "DEBUG", "false")
    }
}
