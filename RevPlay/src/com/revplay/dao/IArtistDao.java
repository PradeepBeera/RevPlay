package com.revplay.dao;

import com.revplay.model.ArtistAccount;
import java.util.List;

public interface IArtistDao {
    boolean registerArtist(ArtistAccount artist);
    ArtistAccount getArtistByEmail(String email);
    ArtistAccount getArtistById(int id);
    boolean updateArtistProfile(ArtistAccount artist);
    List<ArtistAccount> getAllArtists();
    boolean deleteArtist(int artistId);
}
