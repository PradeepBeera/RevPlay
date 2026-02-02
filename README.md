# 🎵 RevPlay – Console-Based Music Streaming Application

## 📌 Overview
RevPlay is a console-based music streaming application that allows users to explore music, manage playlists, mark favorites, and simulate music playback.  

The application also supports **musician/artist accounts** for uploading songs, managing albums, and viewing listening statistics.

The system is built using a **modular, layered architecture** and is designed to be easily extensible into a **web or mobile application** in future phases.

---

## 🚀 Features

### 👤 User (Listener) Features
- Register and login to an account  
- Search songs, artists, albums, playlists, and podcasts  
- Browse content by genre, artist, and album  
- Mark songs as favorites and view favorite list  
- Create public or private playlists  
- Add and remove songs from playlists  
- Update playlist details (name, description, privacy)  
- Delete personal playlists  
- View public playlists created by other users  
- Simulated music player controls (Play, Pause, Skip, Repeat)  
- View recently played songs  
- View listening history  
- Change password and recover forgotten password  

---

### 🎤 Artist / Musician Features
- Register and login as an artist  
- Create and manage artist profile (bio, genre, social links)  
- Upload songs with metadata (title, album, genre, duration, release date)  
- Create albums and add songs to albums  
- View, update, and delete uploaded songs and albums  
- Track play count and song statistics  
- View users who have added songs to favorites  

---

## 🧱 Architecture
The application follows a **modular layered architecture**:

- **Presentation Layer** – Console-based UI  
- **Service Layer** – Business logic  
- **Data Access Layer (DAO)** – Database operations  
- **Model Layer** – Entity classes  
- **Utility Layer** – Validation and logging  
- **Test Layer** – Unit and integration tests  

This design ensures **separation of concer**
