package com.revplay.service;

import com.revplay.model.Song;
import java.util.List;

public interface ISongService {
    boolean uploadSong(Song song);

    List<Song> getAllSongs();

    List<Song> getArtistSongs(int artistId);

    List<Song> search(String keyword);

    boolean playSong(int songId);

    boolean playSong(int songId, int userId); // For history

    Song getSongDetails(int songId);
}
