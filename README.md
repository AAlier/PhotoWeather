# Photo Weather App
Photo Weather App is an Android application that allows users to capture photos and add real-time weather information as a banner overlay. It leverages Jetpack Compose as the primary UI framework to deliver a modern and intuitive user interface.

# Features
- Capture photos: Users can use the device's camera to take photos within the app.
- Weather overlay: The app integrates with a free weather data provider, such as the OpenWeatherMap API, to fetch current weather information based on the user's location. The weather information, including place name, temperature, and weather condition, is dynamically added as a banner overlay on top of the captured photo.
- History list: Weather photos are automatically saved in a history list, allowing users to revisit and view their photos at any time. Both thumbnail and full-size views are provided for a comprehensive browsing experience.
- Seamless sharing: Users can easily share their weather photos with friends through other apps installed on their device, making it effortless to spread the captured moments.

# Requirements
To build and run the Photo Weather App locally, make sure you have the following requirements fulfilled:

- Android Studio [version]
- Kotlin [version]
- Android SDK [version]

# Installation
Clone the repository: git clone [repository URL](https://github.com/AAlier/PhotoWeather)
- Open the project in Android Studio.
- Build and run the app on an Android device or emulator.

# Usage
Launch the Photo Weather App on your Android device.
Allow the app to access your device's camera and location.
Capture a photo using the camera within the app.
The app will automatically retrieve and add weather information as a banner overlay on top of the photo.
Save the weather photo to the history list for future reference.
Browse the history list to view your weather photos in thumbnail and full-size views.
Share your weather photos with friends through other installed apps on your device.

# Testing
The Photo Weather App includes the following testing options:

- Unit Testing: [Instructions for running unit tests]
- UI Testing: [Instructions for running UI tests]

# Contributing
Contributions to the Photo Weather App are welcome! If you'd like to contribute, please follow these steps:

- Fork the repository.
- Create a new branch for your feature or bug fix.
- Make your changes and ensure that the app is still functioning correctly.
- Commit and push your changes to your forked repository.
- Create a pull request detailing your changes and submit it for review.

- Library 1: [Retrofit](https://square.github.io/retrofit/) For network requests. Retrofit turns your HTTP API into a Java interface.
- Library 2: [Koin](https://github.com/InsertKoinIO/koin) a pragmatic lightweight dependency injection framework for Kotlin developers
- Library 3: [Room](https://developer.android.com/training/data-storage/room) The Room persistence library provides an abstraction layer over SQLite to allow fluent database access while harnessing the full power of SQLite
- Library 4: [Timber](https://github.com/JakeWharton/timber) This is a logger with a small, extensible API which provides utility on top of Android's normal Log class.