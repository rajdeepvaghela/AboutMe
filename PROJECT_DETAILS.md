# Project Details: AboutMe

## Overview
Personal portfolio showcase built with Compose Multiplatform (CMP) and Kotlin Multiplatform (KMP). Targets Android, iOS, Desktop (JVM), and Web (WasmJS/JS).

## Tech Stack
- **UI Framework**: Compose Multiplatform
- **Language**: Kotlin Multiplatform
- **DI**: Koin
- **Storage**: KStore
- **Network**: Ktor
- **Images**: Coil / Sketch
- **Config**: BuildKonfig
- **Backend/Analytics**: Supabase (Postgrest-kt)

## Project Structure

### Root
- `composeApp/`: Main application module.
- `iosApp/`: Xcode project for iOS target.
- `gradle/`: Gradle wrapper and configuration.
- `kotlin-js-store/`: JS-specific storage implementation.
- `secret.properties`: Supabase credentials (ignored by git).
- `libs.versions.toml`: Dependency version catalog.

### composeApp/src
- `commonMain/`: 90% of logic. Contains ViewModels, Repositories, and shared UI.
    - `composeResources/`: Shared assets (drawables, fonts, values).
- `androidMain/`: Android-specific API implementations and manifest.
- `iosMain/`: iOS-specific API implementations.
- `jvmMain/`: Desktop-specific API implementations.
- `wasmJsMain/`: Web (Wasm) specific implementations.
- `jsMain/`: Web (JS) specific implementations.
- `webMain/`: Web resources (HTML, CSS, Favicon).

## Key Architecture
- **Clean Architecture**: Heavy emphasis on `commonMain` for business logic.
- **Platform Abstraction**: Platform-specific code handled via `expect`/`actual` or DI modules in platform source sets.
