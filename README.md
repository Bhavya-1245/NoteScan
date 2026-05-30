# NoteScan (In Progress - BAckend Work)

NoteScan is a multi-module project with an Android client and a Spring Boot backend.

## Overview

- Android client built with Kotlin and AndroidX.
- Uses ML Kit text recognition for multiple scripts and languages.
- Backend provides a simple WebSocket STOMP endpoint for real-time messaging.

## Project structure

- `app/` — Android Gradle project
  - `app/app/` — Android application module
  - `app/gradle/` — version catalogs and Gradle wrapper files
- `backend/` — Spring Boot backend service
  - `backend/src/main/java/com/noteS/backend` — application entrypoint, WebSocket config, and messaging models

## Key features

- Text recognition support for:
  - Latin
  - Chinese
  - Devanagari
  - Japanese
  - Korean
- Android client dependencies include Retrofit, Gson, and ML Kit text recognition libraries.
- Backend dependencies include Spring Boot WebSocket support and Lombok.

## Build and run

### Android client

From the `app/` directory:

```bash
./gradlew assembleDebug
```

On Windows use:

```bash
gradlew.bat assembleDebug
```

### Backend service

From the `backend/` directory:

```bash
./mvnw spring-boot:run
```

On Windows use:

```bash
mvnw.cmd spring-boot:run
```

## Notes

- Android app targets SDK 36 and supports min SDK 24.
- Backend runs on Java 17 and exposes a STOMP endpoint at `/stomp-endpoint`.