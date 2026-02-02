package com.revplay.dao;

import com.revplay.model.Playlist;
import com.revplay.model.Song;
import com.revplay.util.JDBCUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDaoImpl implements IPlaylistDao {

    @Override
    public boolean createPlaylist(Playlist playlist) {
        String sql = "INSERT INTO PLAYLIST (user_id, name, description, privacy_status, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, playlist.getUserId());
            ps.setString(2, playlist.getName());
            ps.setString(3, playlist.getDescription());
            ps.setString(4, playlist.getPrivacyStatus());
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in createPlaylist: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error creating playlist", e);
        }
    }

    @Override
    public boolean deletePlaylist(int playlistId) {
        String sql = "DELETE FROM PLAYLIST WHERE playlist_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, playlistId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in deletePlaylist: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error deleting playlist", e);
        }
    }

    @Override
    public boolean addSongToPlaylist(int playlistId, int songId) {
        String sql = "INSERT INTO PLAYLIST_SONG (playlist_id, song_id, added_at) VALUES (?, ?, ?)";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, playlistId);
            ps.setInt(2, songId);
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));

            if (ps.executeUpdate() > 0) {
                updateTimestamp(playlistId);
                return true;
            }
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in addSongToPlaylist: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error adding song to playlist", e);
        }
        return false;
    }

    @Override
    public boolean removeSongFromPlaylist(int playlistId, int songId) {
        String sql = "DELETE FROM PLAYLIST_SONG WHERE playlist_id = ? AND song_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, playlistId);
            ps.setInt(2, songId);

            if (ps.executeUpdate() > 0) {
                updateTimestamp(playlistId);
                return true;
            }
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in removeSongFromPlaylist: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error removing song from playlist", e);
        }
        return false;
    }

    private void updateTimestamp(int playlistId) {
        String sql = "UPDATE PLAYLIST SET updated_at = ? WHERE playlist_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(2, playlistId);
            ps.executeUpdate();
        } catch (SQLException e) {
        }
    }

    @Override
    public List<Playlist> getPlaylistsByUserId(int userId) {
        List<Playlist> playlists = new ArrayList<>();
        String sql = "SELECT * FROM PLAYLIST WHERE user_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Playlist p = new Playlist();
                p.setPlaylistId(rs.getInt("playlist_id"));
                p.setUserId(rs.getInt("user_id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setPrivacyStatus(rs.getString("privacy_status"));
                p.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                p.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                playlists.add(p);
            }
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in getPlaylistsByUserId: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error fetching playlists", e);
        }
        return playlists;
    }

    @Override
    public List<Song> getSongsInPlaylist(int playlistId) {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT s.*, g.genre_name FROM SONG s " +
                "JOIN PLAYLIST_SONG ps ON s.song_id = ps.song_id " +
                "LEFT JOIN GENRE g ON s.genre_id = g.genre_id " +
                "WHERE ps.playlist_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, playlistId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Song song = new Song();
                song.setSongId(rs.getInt("song_id"));
                song.setTitle(rs.getString("title"));
                song.setAlbumId(rs.getInt("album_id"));
                if (rs.wasNull())
                    song.setAlbumId(null);
                song.setArtistId(rs.getInt("artist_id"));
                song.setGenreId(rs.getInt("genre_id"));
                song.setDurationSeconds(rs.getInt("duration_seconds"));
                song.setReleaseDate(rs.getDate("release_date").toLocalDate());
                song.setPlayCount(rs.getInt("play_count"));
                song.setIsActive(rs.getString("is_active"));
                try {
                    song.setGenreName(rs.getString("genre_name"));
                } catch (Exception e) {
                }
                songs.add(song);
            }
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in getSongsInPlaylist: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error fetching songs in playlist", e);
        }
        return songs;
    }
}
