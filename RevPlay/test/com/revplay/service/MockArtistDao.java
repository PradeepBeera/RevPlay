package com.revplay.service;

import com.revplay.dao.IArtistDao;
import com.revplay.model.ArtistAccount;
import java.util.*;

/**
 * Mock implementation of IArtistDao for unit testing.
 */
public class MockArtistDao implements IArtistDao {
    private Map<Integer, ArtistAccount> artists = new HashMap<>();
    private Map<String, ArtistAccount> artistsByEmail = new HashMap<>();
    private int nextId = 1;

    @Override
    public boolean registerArtist(ArtistAccount artist) {
        if (artist.getEmail() != null && artistsByEmail.containsKey(artist.getEmail())) {
            return false;
        }
        artist.setArtistId(nextId++);
        artists.put(artist.getArtistId(), artist);
        if (artist.getEmail() != null) {
            artistsByEmail.put(artist.getEmail(), artist);
        }
        return true;
    }

    @Override
    public ArtistAccount getArtistByEmail(String email) {
        return artistsByEmail.get(email);
    }

    @Override
    public ArtistAccount getArtistById(int id) {
        return artists.get(id);
    }

    @Override
    public boolean updateArtistProfile(ArtistAccount artist) {
        if (!artists.containsKey(artist.getArtistId())) {
            return false;
        }
        artists.put(artist.getArtistId(), artist);
        return true;
    }

    @Override
    public List<ArtistAccount> getAllArtists() {
        return new ArrayList<>(artists.values());
    }

    @Override
    public boolean deleteArtist(int artistId) {
        ArtistAccount removed = artists.remove(artistId);
        if (removed != null && removed.getEmail() != null) {
            artistsByEmail.remove(removed.getEmail());
        }
        return removed != null;
    }

    public void clear() {
        artists.clear();
        artistsByEmail.clear();
        nextId = 1;
    }
}
