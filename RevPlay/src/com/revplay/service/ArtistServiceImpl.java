package com.revplay.service;

import com.revplay.dao.ArtistDaoImpl;
import com.revplay.dao.IArtistDao;
import com.revplay.model.ArtistAccount;

import java.util.List;

public class ArtistServiceImpl implements IArtistService {

    private IArtistDao artistDao = new ArtistDaoImpl();

    public void setArtistDao(IArtistDao artistDao) {
        this.artistDao = artistDao;
    }

    @Override
    public boolean registerArtist(ArtistAccount artist) {
        com.revplay.util.LoggerUtil.logInfo("Attempting to register artist: " + artist.getEmail());
        ArtistAccount existing = artistDao.getArtistByEmail(artist.getEmail());
        if (existing != null) {
            com.revplay.util.LoggerUtil.logWarning("Artist registration failed: Email exists - " + artist.getEmail());
            return false;
        }
        boolean isRegistered = artistDao.registerArtist(artist);
        if (isRegistered) {
            com.revplay.util.LoggerUtil.logInfo("Artist registered successfully: " + artist.getEmail());
        } else {
            com.revplay.util.LoggerUtil.logWarning("Artist registration failed for: " + artist.getEmail());
        }
        return isRegistered;
    }

    @Override
    public ArtistAccount getArtistByEmail(String email) {
        return artistDao.getArtistByEmail(email);
    }

    @Override
    public ArtistAccount login(String email, String password) {
        com.revplay.util.LoggerUtil.logInfo("Artist login attempt: " + email);
        ArtistAccount artist = artistDao.getArtistByEmail(email);
        if (artist != null && artist.getPasswordHash().equals(password)) {
            com.revplay.util.LoggerUtil.logInfo("Artist login successful: " + email);
            return artist;
        }
        com.revplay.util.LoggerUtil.logWarning("Artist login failed: " + email);
        return null;
    }

    @Override
    public boolean updateProfile(ArtistAccount artist) {
        return artistDao.updateArtistProfile(artist);
    }

    @Override
    public ArtistAccount getArtistProfile(int artistId) {
        return artistDao.getArtistById(artistId);
    }

    @Override
    public List<ArtistAccount> getAllArtists() {
        return artistDao.getAllArtists();
    }
}
