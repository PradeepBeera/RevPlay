package com.revplay.service;

import com.revplay.dao.IPlaylistDao;
import com.revplay.dao.PlaylistDaoImpl;
import com.revplay.model.Playlist;
import com.revplay.model.Song;

import java.util.List;

public class PlaylistServiceImpl implements IPlaylistService {

    private IPlaylistDao playlistDao = new PlaylistDaoImpl();

    public void setPlaylistDao(IPlaylistDao playlistDao) {
        this.playlistDao = playlistDao;
    }

    @Override
    public boolean createPlaylist(Playlist playlist) {
        return playlistDao.createPlaylist(playlist);
    }

    @Override
    public boolean deletePlaylist(int playlistId) {
        return playlistDao.deletePlaylist(playlistId);
    }

    @Override
    public boolean addSong(int playlistId, int songId) {
        return playlistDao.addSongToPlaylist(playlistId, songId);
    }

    @Override
    public boolean removeSong(int playlistId, int songId) {
        return playlistDao.removeSongFromPlaylist(playlistId, songId);
    }

    @Override
    public List<Playlist> getUserPlaylists(int userId) {
        return playlistDao.getPlaylistsByUserId(userId);
    }

    @Override
    public List<Song> getPlaylistSongs(int playlistId) {
        return playlistDao.getSongsInPlaylist(playlistId);
    }
}
