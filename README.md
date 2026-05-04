# NoteScan

NoteScan is a multi-module project consisting of an Android mobile application and a Spring Boot backend. The Android app uses camera access and on-device text recognition to scan and interpret text, while the backend provides a WebSocket/STOMP server for real-time messaging support.

## Project structure

- `app/` - Android Gradle project
  - `app/` - Android application module
  - `build.gradle.kts` - top-level Gradle configuration
  - `settings.gradle.kts` - Gradle module settings
- `backend/` - Spring Boot backend service
  - `pom.xml` - Maven configuration
  - `src/main/java/com/noteS/backend` - backend source code

## Android app

The Android module is configured for:
- `compileSdk = 36`
- `minSdk = 24`
- Kotlin support
- Camera permission and file provider support
- Retrofit for networking
- Gson and Scalars converter support
- ML Kit text recognition for multiple scripts (Latin, Chinese, Devanagari, Japanese, Korean)

Key files:
- `app/app/src/main/AndroidManifest.xml` - app permissions and provider declarations
- `app/app/build.gradle.kts` - Android dependencies and build settings

## Backend service

The Spring Boot backend provides a WebSocket/STOMP endpoint for real-time messaging.

Key components:
- `backend/src/main/java/com/noteS/backend/BackendApplication.java` - Spring Boot entry point
- `backend/src/main/java/com/noteS/backend/WebSocketConfiguration.java` - WebSocket and STOMP configuration
- `backend/src/main/java/com/noteS/backend/GreetingController.java` - message handling controller

## Requirements

- Java 17
- Android SDK 36
- Android Studio or Gradle command line
- Maven for backend build

## Build and run

### Run backend

From the `backend` directory:

```bash
./mvnw spring-boot:run
```

### Run Android app

Open `app` in Android Studio and run the `app` module on an emulator or device.

Or from the `app` root directory:

```bash
./gradlew :app:installDebug
```

## Notes

- The backend is currently configured with WebSocket/STOMP endpoints.
- The Android app requests camera and internet permissions and supports cleartext traffic in the manifest.
- Update package names and endpoint URLs as needed for production use.
