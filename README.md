# AboutMe

AboutMe is my personal portfolio project built with **Compose Multiplatform (CMP)**. It serves as a living showcase of my technical skills, integrating custom open-source KMP libraries and demonstrating a seamless user experience across Android, iOS, Desktop (JVM), and Web (WasmJS as default, JS as fallback).

- [Website (Wasm)](https://rajdeepvaghela.github.io)
- [Download Android .apk](https://github.com/rajdeepvaghela/AboutMe/releases/latest/download/AboutMe-Android.apk)
- [Download iOS .ipa](https://github.com/rajdeepvaghela/AboutMe/releases/latest/download/AboutMe-iOS.ipa)
- [Download MacOS .dmg](https://github.com/rajdeepvaghela/AboutMe/releases/latest/download/AboutMe-MacOS.dmg)
- [Download Linux .deb](https://github.com/rajdeepvaghela/AboutMe/releases/latest/download/AboutMe-Linux.deb)
- [Download Windows .exe](https://github.com/rajdeepvaghela/AboutMe/releases/latest/download/AboutMe-Windows.exe)

## 🚀 Tech Stack

### Core Framework
- **Compose Multiplatform**: Unified UI framework for all targets.
- **Kotlin Multiplatform (KMP)**: Shared business logic across platforms.

### Architecture & Infrastructure
- **Dependency Injection**: [Koin](https://insert-koin.io/) for scalable and decoupled component management.
- **Local Storage**: [KStore](https://github.com/xxfast/KStore) for lightweight, type-safe persistent storage.
- **Networking**: [Ktor](https://ktor.io/) for asynchronous HTTP requests.
- **Image Loading**: Integrated with [Coil](https://coil-kt.github.io/coil/) and [Sketch](https://github.com/panpf/sketch) for high-performance image rendering.
- **Configuration**: [BuildKonfig](https://github.com/codingfeline/buildkonfig) for managing environment-specific secrets and constants.

## 🛠 Integrations

### Supabase Integration
The project leverages **Supabase** primarily for **analytics tracking**, allowing for the monitoring of user interactions and portfolio performance.
- **Postgrest-kt**: Used for type-safe database queries and logging events.

### Open Source Libraries
This project serves as a demonstration ground for several specialized KMP UI components developed by me to enhance interactivity:
- **ValuePickerSlider**: Advanced slider for precise value selection.
- **ViewSlider**: Custom slider implementation for refined UI control.
- **CircularList**: An intuitive circular layout for list items.
- **Stepper**: A streamlined stepper component for multi-step process navigation.

## 🏗 Project Architecture

The project follows a clean architecture approach with a heavy emphasis on the **Shared Common** module:

- `commonMain`: Contains 90% of the application logic, including ViewModels, Repositories, and others.
- `androidMain`, `iosMain`, `jvmMain`, `jsMain`, `wasmJsMain`: Platform-specific implementations for low-level APIs (e.g., KStore file systems, Ktor engines).

## ⚙️ Getting Started

### Prerequisites
- JDK 17+
- Android Studio / IntelliJ IDEA
- Xcode (for iOS development)

### Configuration
To run the project, this app needs Supabase credentials. Create a `secret.properties` file in the root directory:

```properties
SUPABASE_URL=your_supabase_url
SUPABASE_KEY=your_supabase_anon_key
```

### Running the App
- **Android**: Run the `composeApp` configuration via Android Studio.
- **iOS**: Open `iosApp/iosApp.xcodeproj` in Xcode or run from Android Studio.
- **Desktop**: Execute the JVM target via Gradle.
- **Web/Wasm**: Run the `wasmJsBrowserRun` or `jsBrowserRun` Gradle tasks.
