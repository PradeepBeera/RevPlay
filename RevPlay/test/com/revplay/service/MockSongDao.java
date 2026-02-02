package com.revplay.service;

import com.revplay.dao.ISongDao;
import com.revplay.model.Song;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Mock implementation of ISongDao for unit testing.
 */
public class MockSongDao implements ISongDao {
    private Map<Integer, Song> songs = new HashMap<>();
    private int nextId = 1;

    @Override
    public boolean addSong(Song song) {
        song.setSongId(nextId++);
        songs.put(song.getSongId(), song);
        return true;
    }

    @Override
    public Song getSongById(int songId) {
        return songs.get(songId);
    }

    @Override
    public List<Song> getSongsByArtistId(int artistId) {
        return songs.values().stream()
                .filter(s -> s.getArtistId() == artistId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Song> getSongsByAlbumId(int albumId) {
        return songs.values().stream()
                .filter(s -> s.getAlbumId() != null && s.getAlbumId() == albumId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Song> searchSongs(String keyword) {
        return songs.values().stream()
                .filter(s -> s.getTitle() != null && s.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteSong(int songId) {
        return songs.remove(songId) != null;
    }

    @Override
    public boolean incrementPlayCount(int songId) {
        Song s = songs.get(songId);
        if (s == null)
            return false;
        s.setPlayCount(s.getPlayCount() + 1);
        return true;
    }

    public void clear() {
        songs.clear();
        nextId = 1;
    }
}
