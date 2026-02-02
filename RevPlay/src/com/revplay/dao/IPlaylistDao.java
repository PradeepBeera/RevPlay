package com.revplay.dao;

import com.revplay.model.Playlist;
import com.revplay.model.Song;
import java.util.List;

public interface IPlaylistDao {
    boolean createPlaylist(Playlist playlist);

    boolean deletePlaylist(int playlistId);

    boolean addSongToPlaylist(int playlistId, int songId);

    boolean removeSongFromPlaylist(int playlistId, int songId);

    List<Playlist> getPlaylistsByUserId(int userId);

    List<Song> getSongsInPlaylist(int playlistId);
}
