package com.revplay.service;

import com.revplay.model.Playlist;
import com.revplay.model.Song;
import java.util.List;

public interface IPlaylistService {
    boolean createPlaylist(Playlist playlist);

    boolean deletePlaylist(int playlistId);

    boolean addSong(int playlistId, int songId);

    boolean removeSong(int playlistId, int songId);

    List<Playlist> getUserPlaylists(int userId);

    List<Song> getPlaylistSongs(int playlistId);
}
