package com.revplay.service;

import com.revplay.model.Playlist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Unit tests for PlaylistService using mock DAO.
 */
public class PlaylistServiceTest {

    private MockPlaylistDao mockPlaylistDao;
    private PlaylistServiceImpl playlistService;

    @BeforeEach
    public void setup() {
        mockPlaylistDao = new MockPlaylistDao();
        playlistService = new PlaylistServiceImpl();
        playlistService.setPlaylistDao(mockPlaylistDao);
    }

    @Test
    public void testCreatePlaylist_Success() {
        Playlist playlist = new Playlist();
        playlist.setUserId(1);
        playlist.setName("My Favorites");
        playlist.setPrivacyStatus("PUBLIC");

        boolean result = playlistService.createPlaylist(playlist);

        assertTrue(result, "Playlist creation should succeed");
        assertEquals(1, playlist.getPlaylistId());
    }

    @Test
    public void testGetUserPlaylists() {
        Playlist p1 = new Playlist();
        p1.setUserId(5);
        p1.setName("Playlist 1");
        mockPlaylistDao.createPlaylist(p1);

        Playlist p2 = new Playlist();
        p2.setUserId(5);
        p2.setName("Playlist 2");
        mockPlaylistDao.createPlaylist(p2);

        Playlist p3 = new Playlist();
        p3.setUserId(10);
        p3.setName("Other User Playlist");
        mockPlaylistDao.createPlaylist(p3);

        List<Playlist> playlists = playlistService.getUserPlaylists(5);

        assertNotNull(playlists);
        assertEquals(2, playlists.size());
    }

    @Test
    public void testDeletePlaylist_Success() {
        Playlist playlist = new Playlist();
        playlist.setUserId(1);
        playlist.setName("Delete Me");
        mockPlaylistDao.createPlaylist(playlist);
        int playlistId = playlist.getPlaylistId();

        boolean result = playlistService.deletePlaylist(playlistId);

        assertTrue(result, "Delete playlist should succeed");
    }

    @Test
    public void testDeletePlaylist_NonExistent() {
        boolean result = playlistService.deletePlaylist(999);

        assertFalse(result, "Deleting non-existent playlist should fail");
    }

    @Test
    public void testAddSong_Success() {
        Playlist playlist = new Playlist();
        playlist.setUserId(1);
        playlist.setName("Songs Playlist");
        mockPlaylistDao.createPlaylist(playlist);

        boolean result = playlistService.addSong(playlist.getPlaylistId(), 100);

        assertTrue(result);
    }

    @Test
    public void testRemoveSong() {
        Playlist playlist = new Playlist();
        playlist.setUserId(1);
        playlist.setName("Remove Song Test");
        mockPlaylistDao.createPlaylist(playlist);
        mockPlaylistDao.addSongToPlaylist(playlist.getPlaylistId(), 50);

        boolean result = playlistService.removeSong(playlist.getPlaylistId(), 50);

        assertTrue(result);
    }
}
