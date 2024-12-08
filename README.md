# Carpooling Application

## Overview
The **Carpooling Application** is an Android app designed to help users find and join carpool rides. It connects drivers and passengers for shared travel, making commuting more convenient, cost-effective, and environmentally friendly.

This app allows users to create or join carpool groups, set their travel preferences, view available rides, and track their rides in real-time. It leverages location services, Google Maps integration, and a user-friendly interface to provide a seamless carpooling experience.

## Features
- **User Authentication**: Register and log in using email or social media accounts.
- **Create a Ride**: Drivers can post their ride details including starting location, destination, date, and time.
- **Join a Ride**: Passengers can search for rides based on their location, destination, and travel time.
- **Ride Matching**: The app suggests potential matches based on user preferences.
- **Ride Notifications**: Users get notifications for upcoming rides, changes, or cancellations.
- **In-App Chat**: In-app messaging feature to communicate between drivers and passengers.
- **Map Integration**: Real-time navigation and location tracking via Google Maps API.
- **User Profile**: Users can set and update their profile, including preferences and vehicle details.

## Technologies Used
- **Android Studio**: IDE used to develop the app.
- **Java/Kotlin**: Programming languages used for app development.
- **MySQL Database**: Used for storing user and ride information.
- **Firebase**: Used for user authentication and push notifications.
- **Google Maps API**: Integrated for real-time mapping, location services, and route planning.
- **Retrofit/Volley**: Used for network operations, such as fetching and posting ride data to a server.

## Database Structure
The app uses **MySQL** as the database for storing ride details, user information, and related data. Here’s a simple structure of the tables:

### 1. **Users Table**
Stores user information such as name, email, password (hashed), phone number, etc.
```sql
CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255),
  email VARCHAR(255) UNIQUE,
  password VARCHAR(255),
  phone VARCHAR(15),
  profile_picture VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
