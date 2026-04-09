import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.buildkonfig)
}

val secretProps = Properties().apply {
    rootProject.file("secret.properties").takeIf { it.exists() }?.inputStream()?.use { load(it) }
}
val secretSupabaseUrl: String = secretProps.getProperty("SUPABASE_URL", "")
val secretSupabaseKey: String = secretProps.getProperty("SUPABASE_KEY", "")


kotlin {

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
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

                // sketch
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

android {
    namespace = "com.rdapps.aboutme"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.rdapps.aboutme"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
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
    }

    defaultConfigs("release") {
        buildConfigField(BOOLEAN, "DEBUG", "false")
    }
}
