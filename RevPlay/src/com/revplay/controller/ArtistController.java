package com.revplay.controller;

import com.revplay.model.*;
import com.revplay.service.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class ArtistController {

    private static IArtistService artistService = new ArtistServiceImpl();
    private static ISongService songService = new SongServiceImpl();
    private static IAlbumService albumService = new AlbumServiceImpl();
    private static IGenreService genreService = new GenreServiceImpl();
    private static IPodcastService podcastService = new PodcastServiceImpl();
    private static Scanner scanner = new Scanner(System.in);

    public static void artistDashboard(ArtistAccount artist) {
        while (true) {
            System.out.println("\n=== Artist Dashboard: " + artist.getStageName() + " ===");
            System.out.println("1. Upload Song");
            System.out.println("2. Create Album");
            System.out.println("3. Manage Podcasts");
            System.out.println("4. View My Songs");
            System.out.println("5. View My Albums");
            System.out.println("0. Logout");
            System.out.print("Enter choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        uploadSong(artist);
                        break;
                    case 2:
                        createAlbum(artist);
                        break;
                    case 3:
                        managePodcasts(artist);
                        break;
                    case 4:
                        viewMySongs(artist);
                        break;
                    case 5:
                        viewMyAlbums(artist);
                        break;
                    case 0:
                        System.out.println("Logging out...");
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }
    }

    private static void uploadSong(ArtistAccount artist) {
        System.out.println("\n--- Upload Song ---");
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();

        System.out.print("Enter Genre Name (e.g., Pop, Rock): ");
        String genreName = scanner.nextLine().trim();
        Genre genre = genreService.getOrCreateGenre(genreName);
        if (genre == null) {
            System.out.println("❌ Failed to process genre.");
            return;
        }

        System.out.print("Enter Duration (seconds): ");
        int duration = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter File URL: ");
        String fileUrl = scanner.nextLine();

        System.out.print("Enter Release Date (YYYY-MM-DD) [Empty for Today]: ");
        String dateStr = scanner.nextLine();
        LocalDate releaseDate = dateStr.isEmpty() ? LocalDate.now() : LocalDate.parse(dateStr);

        System.out.print("Enter Album ID (0 for single): ");
        int albumId = 0;
        try {
            albumId = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
        }

        Song song = new Song();
        song.setTitle(title);
        song.setArtistId(artist.getArtistId());
        song.setGenreId(genre.getGenreId());
        song.setDurationSeconds(duration);
        song.setFileUrl(fileUrl);
        song.setReleaseDate(releaseDate);

        if (albumId > 0) {
            Album album = albumService.getAlbumById(albumId);
            if (album != null && album.getArtistId() == artist.getArtistId()) {
                song.setAlbumId(albumId);
            } else {
                System.out.println("⚠️ Album not found or validation failed. Creating as Single.");
                song.setAlbumId(null);
            }
        }

        if (songService.uploadSong(song)) {
            System.out.println("✅ Song uploaded successfully!");
        } else {
            System.out.println("❌ Upload failed.");
        }
    }

    private static void createAlbum(ArtistAccount artist) {
        System.out.println("\n--- Create Album ---");
        System.out.print("Enter Album Title: ");
        String title = scanner.nextLine();

        System.out.print("Enter Description: ");
        String description = scanner.nextLine();

        System.out.print("Enter Cover Image URL: ");
        String coverUrl = scanner.nextLine();

        System.out.print("Enter Release Date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        LocalDate releaseDate = dateStr.isEmpty() ? LocalDate.now() : LocalDate.parse(dateStr);

        Album album = new Album();
        album.setArtistId(artist.getArtistId());
        album.setTitle(title);
        album.setDescription(description);
        album.setCoverImageUrl(coverUrl);
        album.setReleaseDate(releaseDate);

        if (albumService.createAlbum(album)) {
            System.out.println("✅ Album created successfully!");
        } else {
            System.out.println("❌ Failed to create album.");
        }
    }

    private static void managePodcasts(ArtistAccount artist) {
        System.out.println("\n--- Manage Podcasts ---");
        System.out.println("1. Create Podcast");
        System.out.println("2. Add Episode to Podcast");
        System.out.println("0. Back");
        int subChoice = Integer.parseInt(scanner.nextLine());

        if (subChoice == 1) {
            System.out.print("Podcast Title: ");
            String title = scanner.nextLine();
            System.out.print("Host Name: ");
            String host = scanner.nextLine();
            System.out.print("Category: ");
            String cat = scanner.nextLine();
            System.out.print("Description: ");
            String desc = scanner.nextLine();

            Podcast p = new Podcast(0, title, host, cat, desc, LocalDateTime.now());
            if (podcastService.createPodcast(p))
                System.out.println("✅ Podcast created.");
            else
                System.out.println("❌ Failed.");

        } else if (subChoice == 2) {
            System.out.print("Enter Podcast ID: ");
            int pid = Integer.parseInt(scanner.nextLine());

            System.out.print("Episode Title: ");
            String title = scanner.nextLine();
            System.out.print("Duration (sec): ");
            int dur = Integer.parseInt(scanner.nextLine());
            System.out.print("File URL: ");
            String url = scanner.nextLine();

            PodcastEpisode ep = new PodcastEpisode(0, pid, title, dur, LocalDate.now(), url, 0, LocalDateTime.now());
            if (podcastService.addEpisode(ep))
                System.out.println("✅ Episode added.");
            else
                System.out.println("❌ Failed.");
        }
    }

    private static void viewMySongs(ArtistAccount artist) {
        List<Song> songs = songService.getArtistSongs(artist.getArtistId());
        if (songs.isEmpty()) {
            System.out.println("No songs found.");
        } else {
            System.out.println("\n--- My Songs ---");
            for (Song s : songs) {
                System.out.println("ID: " + s.getSongId() +
                        " | Title: " + s.getTitle() +
                        " | Genre: " + s.getGenreName() +
                        " | Plays: " + s.getPlayCount() +
                        " | URL: " + s.getFileUrl());
            }
        }
    }

    private static void viewMyAlbums(ArtistAccount artist) {
        List<Album> albums = albumService.getArtistAlbums(artist.getArtistId());
        if (albums.isEmpty()) {
            System.out.println("No albums found.");
        } else {
            System.out.println("\n--- My Albums ---");
            for (Album a : albums) {
                System.out.println(
                        "ID: " + a.getAlbumId() + " | Title: " + a.getTitle() + " | Desc: " + a.getDescription());
            }
        }
    }
}
