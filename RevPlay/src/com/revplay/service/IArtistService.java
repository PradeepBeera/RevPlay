package com.revplay.service;

import com.revplay.model.ArtistAccount;
import java.util.List;

public interface IArtistService {
    boolean registerArtist(ArtistAccount artist);

    ArtistAccount getArtistByEmail(String email);

    ArtistAccount login(String email, String password);

    boolean updateProfile(ArtistAccount artist);

    ArtistAccount getArtistProfile(int artistId);

    List<ArtistAccount> getAllArtists();
}
