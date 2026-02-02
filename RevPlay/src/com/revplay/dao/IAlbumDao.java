package com.revplay.dao;

import com.revplay.model.Album;
import java.util.List;

public interface IAlbumDao {
    boolean createAlbum(Album album);

    Album getAlbumById(int albumId);

    List<Album> getAlbumsByArtistId(int artistId);

    boolean deleteAlbum(int albumId);

    List<Album> getAllAlbums();
}
