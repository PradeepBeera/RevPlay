package com.revplay.service;

import com.revplay.dao.IPlaylistDao;
import com.revplay.model.Playlist;
import com.revplay.model.Song;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Mock implementation of IPlaylistDao for unit testing.
 */
public class MockPlaylistDao implements IPlaylistDao {
    private Map<Integer, Playlist> playlists = new HashMap<>();
    private Map<Integer, List<Integer>> playlistSongs = new HashMap<>(); // playlistId -> songIds
    private int nextId = 1;

    @Override
    public boolean createPlaylist(Playlist playlist) {
        playlist.setPlaylistId(nextId++);
        playlists.put(playlist.getPlaylistId(), playlist);
        playlistSongs.put(playlist.getPlaylistId(), new ArrayList<>());
        return true;
    }

    @Override
    public boolean deletePlaylist(int playlistId) {
        playlistSongs.remove(playlistId);
        return playlists.remove(playlistId) != null;
    }

    @Override
    public boolean addSongToPlaylist(int playlistId, int songId) {
        List<Integer> songs = playlistSongs.get(playlistId);
        if (songs == null)
            return false;
        if (!songs.contains(songId)) {
            songs.add(songId);
        }
        return true;
    }

    @Override
    public boolean removeSongFromPlaylist(int playlistId, int songId) {
        List<Integer> songs = playlistSongs.get(playlistId);
        if (songs == null)
            return false;
        return songs.remove(Integer.valueOf(songId));
    }

    @Override
    public List<Playlist> getPlaylistsByUserId(int userId) {
        return playlists.values().stream()
                .filter(p -> p.getUserId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Song> getSongsInPlaylist(int playlistId) {
        // This would need a song dao reference to fully implement
        // For mock purposes, returning empty list
        return new ArrayList<>();
    }

    public void clear() {
        playlists.clear();
        playlistSongs.clear();
        nextId = 1;
    }
}
