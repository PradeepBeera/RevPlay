package com.revplay.controller;

import com.revplay.dao.FavoriteDaoImpl;
import com.revplay.dao.IFavoriteDao;
import com.revplay.dao.IListeningHistoryDao;
import com.revplay.dao.ListeningHistoryDaoImpl;
import com.revplay.model.*;
import com.revplay.service.*;

import java.util.List;
import java.util.Scanner;

public class UserController {

    private static ISongService songService = new SongServiceImpl();
    private static IPlaylistService playlistService = new PlaylistServiceImpl();
    private static IGenreService genreService = new GenreServiceImpl();
    private static IAlbumService albumService = new AlbumServiceImpl();
    private static IFavoriteDao favoriteDao = new FavoriteDaoImpl();
    private static IListeningHistoryDao historyDao = new ListeningHistoryDaoImpl();
    private static IPodcastService podcastService = new PodcastServiceImpl();
    private static IUserAccountService userService = new UserAccountServiceImpl();
    private static Scanner scanner = new Scanner(System.in);

    public static void userDashboard(UserAccount user) {
        while (true) {
            System.out.println("\n=== User Dashboard: " + user.getFullName() + " ===");
            System.out.println("1. View All Songs");
            System.out.println("2. Browse Albums");
            System.out.println("3. Browse by Genre");
            System.out.println("4. Search Songs");
            System.out.println("5. My Playlists");
            System.out.println("6. My Favorites");
            System.out.println("7. Podcasts");
            System.out.println("8. Listening History");
            System.out.println("9. Update Password");
            System.out.println("0. Logout");
            System.out.print("Enter choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        displayAndInteractSongs(songService.getAllSongs(), user);
                        break;
                    case 2:
                        browseAlbums(user);
                        break;
                    case 3:
                        browseByGenre(user);
                        break;
                    case 4:
                        searchSongs(user);
                        break;
                    case 5:
                        managePlaylists(user);
                        break;
                    case 6:
                        viewFavorites(user);
                        break;
                    case 7:
                        browsePodcasts(user);
                        break;
                    case 8:
                        viewListeningHistory(user);
                        break;
                    case 9:
                        updatePassword(user);
                        break;
                    case 0:
                        System.out.println("Logging out...");
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.print("");
            }
        }
    }

    private static void updatePassword(UserAccount user) {
        System.out.println("\n--- Update Password ---");
        System.out.print("Enter Current Password: ");
        String currentPass = scanner.nextLine();

        if (!currentPass.equals(user.getPasswordHash())) {
            System.out.println("❌ Current password is incorrect.");
            return;
        }

        System.out.print("Enter New Password: ");
        String newPass = scanner.nextLine();
        System.out.print("Confirm New Password: ");
        String confirmPass = scanner.nextLine();

        if (!newPass.equals(confirmPass)) {
            System.out.println("❌ Passwords do not match.");
            return;
        }

        user.setPasswordHash(newPass);
        if (userService.updateUserAccount(user)) {
            System.out.println("✅ Password updated successfully!");
        } else {
            System.out.println("❌ Failed to update password.");
        }
    }

    private static void browseAlbums(UserAccount user) {
        List<Album> albums = albumService.getAllAlbums();
        if (albums.isEmpty()) {
            System.out.println("No albums found.");
        } else {
            System.out.println("\n--- All Albums ---");
            for (int i = 0; i < albums.size(); i++) {
                System.out.println(
                        (i + 1) + ". " + albums.get(i).getTitle() + " (Desc: " + albums.get(i).getDescription() + ")");
            }
            System.out.print("Select Album # to view songs (0 back): ");
            try {
                int idx = Integer.parseInt(scanner.nextLine());
                if (idx > 0 && idx <= albums.size()) {
                    List<Song> songs = songService.getArtistSongs(albums.get(idx - 1).getArtistId());
                    System.out.println("Select 'Search Songs' to find songs from this album.");
                }
            } catch (Exception e) {
            }
        }
    }

    private static void browsePodcasts(UserAccount user) {
        List<Podcast> podcasts = podcastService.getAllPodcasts();
        if (podcasts.isEmpty()) {
            System.out.println("No podcasts found.");
            return;
        }
        System.out.println("\n--- Podcasts ---");
        for (int i = 0; i < podcasts.size(); i++) {
            System.out
                    .println((i + 1) + ". " + podcasts.get(i).getTitle() + " (" + podcasts.get(i).getCategory() + ")");
        }
        System.out.print("Select Podcast # (0 back): ");
        try {
            int idx = Integer.parseInt(scanner.nextLine());
            if (idx > 0 && idx <= podcasts.size()) {
                Podcast p = podcasts.get(idx - 1);
                viewPodcastEpisodes(p);
            }
        } catch (Exception e) {
        }
    }

    private static void viewPodcastEpisodes(Podcast p) {
        List<PodcastEpisode> eps = podcastService.getPodcastEpisodes(p.getPodcastId());
        if (eps.isEmpty()) {
            System.out.println("No episodes.");
            return;
        }
        System.out.println("\nEpisodes for " + p.getTitle());
        for (int i = 0; i < eps.size(); i++) {
            System.out.println((i + 1) + ". " + eps.get(i).getTitle());
        }
        System.out.print("Select Episode # to Play (0 back): ");
        try {
            int idx = Integer.parseInt(scanner.nextLine());
            if (idx > 0 && idx <= eps.size()) {
                podcastService.playEpisode(eps.get(idx - 1).getEpisodeId());
            }
        } catch (Exception e) {
        }
    }

    private static void viewListeningHistory(UserAccount user) {
        List<Song> history = historyDao.getUserHistory(user.getUserId());
        if (history.isEmpty())
            System.out.println("No history.");
        else {
            System.out.println("\n--- Recently Played ---");
            for (Song s : history)
                System.out.println("- " + s.getTitle());

            System.out.println("\n1. Play a song from history");
            System.out.println("0. Back");
            try {
                if (Integer.parseInt(scanner.nextLine()) == 1) {
                    System.out.print("Enter exact song name: ");
                    String n = scanner.nextLine();
                    for (Song s : history) {
                        if (s.getTitle().equalsIgnoreCase(n)) {
                            songService.playSong(s.getSongId(), user.getUserId());
                            break;
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    private static void searchSongs(UserAccount user) {
        System.out.print("Enter keyword: ");
        String keyword = scanner.nextLine();
        displayAndInteractSongs(songService.search(keyword), user);
    }

    private static void browseByGenre(UserAccount user) {
        List<Genre> genres = genreService.getAllGenres();
        for (Genre g : genres)
            System.out.println(g.getGenreId() + ". " + g.getGenreName());
        System.out.print("Select Genre ID: ");
        try {
            int gid = Integer.parseInt(scanner.nextLine());
            displayAndInteractSongs(songService.search(genreService.getAllGenres().stream()
                    .filter(g -> g.getGenreId() == gid).findFirst().get().getGenreName()), user);
        } catch (Exception e) {
        }
    }

    private static void displayAndInteractSongs(List<Song> songs, UserAccount user) {
        if (songs.isEmpty()) {
            System.out.println("No songs found.");
            return;
        }

        System.out.println("\n--- Songs List ---");
        for (int i = 0; i < songs.size(); i++) {
            Song s = songs.get(i);
            System.out.println(
                    (i + 1) + ". " + s.getTitle() + " (" + s.getGenreName() + ") | Plays: " + s.getPlayCount());
        }

        System.out.println("\nSelect Song # to Play/Interact (0 to Back):");
        try {
            int idx = Integer.parseInt(scanner.nextLine());
            if (idx == 0)
                return;
            if (idx > 0 && idx <= songs.size()) {
                Song selected = songs.get(idx - 1);
                interactWithSong(selected, user, songs, idx - 1);
            }
        } catch (Exception e) {
        }
    }

    private static void interactWithSong(Song song, UserAccount user, List<Song> songList, int currentIndex) {
        boolean playing = false;
        boolean repeat = false;

        while (true) {
            System.out.println("\n🎵 === NOW PLAYING === 🎵");
            System.out.println("   🎶 " + song.getTitle());
            System.out.println("   🎤 Genre: " + song.getGenreName());
            System.out.println("   📊 Play Count: " + song.getPlayCount());
            System.out.println("   🔁 Repeat: " + (repeat ? "ON" : "OFF"));
            System.out.println("=============================");
            System.out.println("1. ▶ Play");
            System.out.println("2. ⏸ Pause");
            System.out.println("3. 🔁 Toggle Repeat");
            System.out.println("4. ⏭ Skip to Next");
            System.out.println("5. ⏮ Previous");
            System.out.println("6. ❤ Add to Favorites");
            System.out.println("7. ➕ Add to Playlist");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");

            try {
                int opt = Integer.parseInt(scanner.nextLine());

                switch (opt) {
                    case 1: // Play
                        playing = true;
                        songService.playSong(song.getSongId(), user.getUserId());
                        // Refresh play count
                        Song refreshed = songService.getSongDetails(song.getSongId());
                        if (refreshed != null) {
                            song.setPlayCount(refreshed.getPlayCount());
                        }
                        break;
                    case 2: // Pause
                        if (playing) {
                            System.out.println("⏸ Paused: " + song.getTitle());
                            playing = false;
                        } else {
                            System.out.println("Song is not playing.");
                        }
                        break;
                    case 3: // Toggle Repeat
                        repeat = !repeat;
                        System.out.println("🔁 Repeat is now " + (repeat ? "ON" : "OFF"));
                        break;
                    case 4: // Skip to Next
                        if (songList != null && currentIndex < songList.size() - 1) {
                            currentIndex++;
                            song = songList.get(currentIndex);
                            System.out.println("⏭ Skipped to: " + song.getTitle());
                            songService.playSong(song.getSongId(), user.getUserId());
                            playing = true;
                        } else if (repeat && songList != null && !songList.isEmpty()) {
                            currentIndex = 0;
                            song = songList.get(currentIndex);
                            System.out.println("🔁 Repeating from start: " + song.getTitle());
                            songService.playSong(song.getSongId(), user.getUserId());
                            playing = true;
                        } else {
                            System.out.println("No more songs in list.");
                        }
                        break;
                    case 5: // Previous
                        if (songList != null && currentIndex > 0) {
                            currentIndex--;
                            song = songList.get(currentIndex);
                            System.out.println("⏮ Previous: " + song.getTitle());
                            songService.playSong(song.getSongId(), user.getUserId());
                            playing = true;
                        } else {
                            System.out.println("Already at the first song.");
                        }
                        break;
                    case 6: // Add to Favorites
                        if (favoriteDao.addFavorite(user.getUserId(), song.getSongId()))
                            System.out.println("❤ Added to Favorites!");
                        else
                            System.out.println("Already in favorites.");
                        break;
                    case 7: // Add to Playlist
                        addToPlaylist(user, song);
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }
    }

    private static void addToPlaylist(UserAccount user, Song song) {
        List<Playlist> playlists = playlistService.getUserPlaylists(user.getUserId());
        if (playlists.isEmpty()) {
            System.out.println("No playlists. Create one first.");
            return;
        }
        for (Playlist p : playlists)
            System.out.println(p.getPlaylistId() + ". " + p.getName());
        System.out.print("Enter Playlist ID: ");
        try {
            int pid = Integer.parseInt(scanner.nextLine());
            playlistService.addSong(pid, song.getSongId());
            System.out.println("✅ Added to playlist.");
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }

    private static void managePlaylists(UserAccount user) {
        System.out.println("\n--- My Playlists ---");
        System.out.println("1. Create Playlist");
        System.out.println("2. View Playlists");
        System.out.println("3. Delete Playlist");
        System.out.println("0. Back");
        System.out.print("Enter choice: ");

        try {
            int ch = Integer.parseInt(scanner.nextLine());

            switch (ch) {
                case 1: // Create
                    System.out.print("Playlist Name: ");
                    String n = scanner.nextLine();
                    System.out.print("Description: ");
                    String d = scanner.nextLine();
                    Playlist p = new Playlist();
                    p.setUserId(user.getUserId());
                    p.setName(n);
                    p.setDescription(d);
                    p.setPrivacyStatus("PRIVATE");
                    if (playlistService.createPlaylist(p)) {
                        System.out.println("✅ Playlist created!");
                    } else {
                        System.out.println("❌ Failed to create playlist.");
                    }
                    break;

                case 2: // View
                    List<Playlist> list = playlistService.getUserPlaylists(user.getUserId());
                    if (list.isEmpty()) {
                        System.out.println("No playlists found.");
                    } else {
                        System.out.println("\nYour Playlists:");
                        for (Playlist pl : list) {
                            System.out.println("  ID: " + pl.getPlaylistId() + " | " + pl.getName() + " ("
                                    + pl.getPrivacyStatus() + ")");
                        }
                    }
                    break;

                case 3: // Delete
                    List<Playlist> deleteList = playlistService.getUserPlaylists(user.getUserId());
                    if (deleteList.isEmpty()) {
                        System.out.println("No playlists to delete.");
                    } else {
                        System.out.println("\nYour Playlists:");
                        for (Playlist pl : deleteList) {
                            System.out.println("  ID: " + pl.getPlaylistId() + " | " + pl.getName());
                        }
                        System.out.print("Enter Playlist ID to delete (0 to cancel): ");
                        int deleteId = Integer.parseInt(scanner.nextLine());
                        if (deleteId > 0) {
                            // Verify the playlist belongs to the user
                            boolean found = deleteList.stream().anyMatch(pl -> pl.getPlaylistId() == deleteId);
                            if (found) {
                                if (playlistService.deletePlaylist(deleteId)) {
                                    System.out.println("✅ Playlist deleted!");
                                } else {
                                    System.out.println("❌ Failed to delete playlist.");
                                }
                            } else {
                                System.out.println("❌ Invalid playlist ID.");
                            }
                        }
                    }
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private static void viewFavorites(UserAccount user) {
        List<Song> favs = favoriteDao.getFavoriteSongs(user.getUserId());
        displayAndInteractSongs(favs, user);
    }
}
