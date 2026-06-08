# NoteScan

NoteScan is a completed multi-module project with an Android client and a Spring Boot backend.

## Overview

- Android client built with Kotlin and AndroidX.
- Uses ML Kit text recognition to capture text from camera images.
- The app sends recognized text to a local PC backend via Retrofit.
- Backend launches a desktop notepad GUI and pastes text received from the Android app.

## Project structure

- `app/` — Android Gradle project
  - `app/app/` — Android application module
  - `app/gradle/` — version catalogs and Gradle wrapper files
- `backend/` — Spring Boot backend service
  - `backend/src/main/java/com/noteS/backend` — Spring Boot server and REST controller
  - `backend/src/main/java/com/noteS/dest` — desktop notepad GUI integration

## Key features

- Android app features:
  - capture photo using the device camera
  - recognize text using ML Kit Latin text recognition
  - copy recognized text to clipboard
  - save and reuse PC IP address for backend connection
  - send recognized text to the backend via `POST /api/v1/scan`
  - sign out using Firebase Authentication
- Backend features:
  - Spring Boot 4 application using Java 17
  - REST endpoint at `/api/v1/scan`
  - desktop notepad GUI receives and displays text from the Android app

## Build and run

### Android client

From the `app/` directory:

```bash
./gradlew assembleDebug
```

On Windows:

```bash
gradlew.bat assembleDebug
```

### Backend service

From the `backend/` directory:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

## Notes

- Android app targets SDK 36 and supports min SDK 24.
- Backend runs on Java 17.
- The Android client sends text to the backend at `http://<pc-ip>:8080/api/v1/scan`.
- The backend opens a desktop notepad GUI and appends received text to the notepad window.
