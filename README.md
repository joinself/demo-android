
# Self Demo

**Self Demo** is an Android application showcasing the integration of the **SelfSDK**. It serves as an example for developers, demonstrating how to implement various use cases of the SelfSDK, such as authentication, verify document, sharing verified credentials like email addresses, ID numbers...  
The application is built using modern Android development practices, featuring a user-friendly interface created with **Jetpack Compose**. It provides a practical guide for developers looking to incorporate decentralized identity features into their own Android applications.

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Setup](#setup)
- [Build](#build)
- [Using the App](#using-the-app)

## Features
Main features of the app:

   - Setup account and connect with server
   - Authenticate with server using biometric authentication
   - Verify email address, passport/id card, and custom credentials
   - Share verified email credentials, verified document credentials, and verified custom credentials
   - Back up and restore account's data

## Prerequisites

Before you begin, ensure you have met the following requirements:

   - Java 17 SDK
   - Kotlin 2.0 and above
   - Gradle 8.11.1 and above
   - Android API 28 and above

## Setup

To get a local copy up and running, follow these simple steps:   
Clone the repository:
```bash
git clone git@github.com:joinself/self-sdk-examples.git
```

## Build
You can build and install from the command line

```bash
cd self-sdk-examples/android/SelfDemo

./gradlew :app:assembleDebug

adb -d install -r app/build/outputs/apk/debug/self-demo-debug.apk
```

__Build release__

```bash
./gradlew :app:assembleRelease

# output SelfDemo/app/build/outputs/apk/release/self-demo-release.apk
```

__Bundle aab file__

```bash
./gradlew :app:bundleRelease

# output SelfDemo/app/build/outputs/bundle/release/self-demo-release.aab
```

## Setup Server

For the Self Demo application to function fully (e.g., to connect for credential verification and sharing), a backend server component needs to be running. 

Follow these steps to set up and run the server from the command line, then copy the address for mobile app to connect.
  
```bash
cd self-sdk-examples/java

./gradlew :self-demo:run
```

Or run with docker image

```bash
docker run --pull=always --rm -it ghcr.io/joinself/self-sdk-demo:java
```

## Using the App

This section guides you through the initial steps to get started with the Self Demo application.

1.  **Initialize the SDK:**  
   Upon first launch, the application will typically initialize the SelfSDK in the background. This process sets up the necessary components for the app to interact with the Self network.

2.  **Register an Account:**  
   Once the SDK is initialized, if you are a new user, you will be prompted to register an account or restore a backup file.

3.  **Connect to the Server:**  
   After successful registration (or if you are a returning user who has already registered), the app will attempt to connect to the above server.

4.  **Select an Option to Start:**  
   Once connected, you will typically land on a main screen.  
   Here, you'll find various options to explore the app's features:    

   - **Authenticate** with the connected server using liveness check.
   - **Verify Credentials**: The app guides you to verify your email address or an ID document. After successful verification, credentials are stored in a secured Self database.
   - **Provide Credentials**: Once the credentials are stored in the Self database, you can share them with the connected server.
   - **Sign Documents**: You receive a document from the connected server and respond with either accept or reject.
   - **Backup**: Self SDK creates a snapshot of the local database, which you need to store in the local file system.
   
   
