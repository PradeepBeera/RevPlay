package com.revplay.dao;

import com.revplay.model.Song;
import java.util.List;

public interface ISongDao {
    boolean addSong(Song song);

    Song getSongById(int songId);

    List<Song> getSongsByArtistId(int artistId);

    List<Song> getSongsByAlbumId(int albumId);

    List<Song> searchSongs(String keyword);

    boolean deleteSong(int songId);

    boolean incrementPlayCount(int songId);
}
