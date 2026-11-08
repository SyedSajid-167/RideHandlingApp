# Ride-Handling App Simulator

A ride-hailing app simulator (Ola/Uber style) built in **Java** for **Android**, as an Object-Oriented Programming mini project.

## Features
- Book a ride with pickup, drop and vehicle choice (Bike, Auto, Cab)
- Estimated fare calculation based on distance and vehicle type
- Live ride status: Booked, Ongoing, Completed, Cancelled
- Cancel or complete a ride
- Rate the ride after completion
- Ride history saved on the device

## OOP Concepts Used
- **Abstraction:** abstract class `Vehicle`
- **Inheritance:** `Bike`, `Auto` and `Cab` extend `Vehicle`
- **Polymorphism:** each vehicle overrides the fare calculation
- **Encapsulation:** private fields with getters and setters in `User`, `Driver` and `Ride`
- **Interfaces:** `Bookable` and `Payable`
- **Exception handling:** custom `InvalidLocationException`
- **Collections:** `ArrayList` and `HashMap`
- **Multithreading:** background thread simulates ride progress
- **File handling:** ride history stored using file I/O

## Screens
1. Home (booking)
2. Ride Details and Tracking
3. Rating
4. Ride History

## Tech Stack
- Language: Java
- IDE: Android Studio
- Emulator: Medium Phone API 37.0

## How to Run
1. Clone this repository.
2. Open it in Android Studio.
3. Wait for Gradle sync to finish.
4. Select an emulator or phone and click Run.

## Download APK
https://drive.google.com/file/d/107c8jkeQ77Wf4gW7nPCcXJuqLJrgpZ5A/view?usp=sharing

## Author
1. Syed Sajid
2. M. Gouthami
3. A. Pradeep Kumar
