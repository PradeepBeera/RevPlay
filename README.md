🎵 RevPlay – Console-Based Music Streaming Application
📌 Overview
RevPlay is a console-based music streaming application that allows users to explore music, manage playlists, mark favorites, and simulate music playback. The application also supports musician/artist accounts for uploading songs, managing albums, and viewing listening statistics.

The system is built using a modular, layered architecture and is designed to be easily extensible into a web or mobile application in future phases.

🚀 Features
👤 User (Listener) Features
Register and login to an account
Search songs, artists, albums, playlists, and podcasts
Browse content by genre, artist, and album
Mark songs as favorites and view favorite list
Create public or private playlists
Add and remove songs from playlists
Update playlist details (name, description, privacy)
Delete personal playlists
View public playlists created by other users
Simulated music player controls (Play, Pause, Skip, Repeat)
View recently played songs
View listening history
Change password and recover forgotten password
🎤 Artist/Musician Features
Register and login as an artist
Create and manage artist profile (bio, genre, social links)
Upload songs with metadata (title, album, genre, duration, release date)
Create albums and add songs to albums
View, update, and delete uploaded songs and albums
Track play count and song statistics
View users who have added songs to favorites
🧱 Architecture
The application follows a modular layered architecture:

Presentation Layer (Console UI)
Service Layer (Business Logic)
Data Access Layer (Repository/DAO)
Model Layer (Entities)
Utility Layer (Validation, Logging)
Test Layer (JUnit & Mockito)
This design ensures separation of concerns, scalability, and maintainability.

🗂️ Database Design
ERD (Entity Relationship Diagram) included in the repository
Core entities: User, Artist, Song, Album, Playlist, Favorites, Listening History
Proper relationships maintained between users, artists, and content
🧪 Testing
Unit testing implemented using JUnit
Dependency mocking using Mockito
Test cases cover:
User authentication
Playlist management
Song and album operations
Favorites and history tracking
Error handling and edge cases
🪵 Logging
Logging implemented using a standard Java logging framework
Logs user actions, system events, and exceptions
Helps in debugging and monitoring application flow
🛠️ Technologies Used
Java
Maven / Gradle
JUnit
Mockito
Log4j / SLF4J
Object-Oriented Programming (OOP)
Modular / Layered Architecture
Git & GitHub
