package com.revplay.service;

import com.revplay.dao.IAlbumDao;
import com.revplay.model.Album;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Mock implementation of IAlbumDao for unit testing.
 */
public class MockAlbumDao implements IAlbumDao {
    private Map<Integer, Album> albums = new HashMap<>();
    private int nextId = 1;

    @Override
    public boolean createAlbum(Album album) {
        album.setAlbumId(nextId++);
        albums.put(album.getAlbumId(), album);
        return true;
    }

    @Override
    public Album getAlbumById(int albumId) {
        return albums.get(albumId);
    }

    @Override
    public List<Album> getAlbumsByArtistId(int artistId) {
        return albums.values().stream()
                .filter(a -> a.getArtistId() == artistId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteAlbum(int albumId) {
        return albums.remove(albumId) != null;
    }

    @Override
    public List<Album> getAllAlbums() {
        return new ArrayList<>(albums.values());
    }

    public void clear() {
        albums.clear();
        nextId = 1;
    }
}
