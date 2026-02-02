package com.revplay.service;

import com.revplay.model.Album;

import java.util.List;

public interface IAlbumService {

    boolean createAlbum(Album album);

    Album getAlbumById(int albumId);

    List<Album> getArtistAlbums(int artistId);

    List<Album> getAllAlbums();
}
