package com.revplay.dao;

import com.revplay.model.Song;
import com.revplay.util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FavoriteDaoImpl implements IFavoriteDao {

    @Override
    public boolean addFavorite(int userId, int songId) {
        String sql = "INSERT INTO FAVORITE_SONG (user_id, song_id, favorited_at) VALUES (?, ?, CURRENT_TIMESTAMP)";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, songId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062 || e.getMessage().contains("Duplicate")) {
                return false;
            }
            com.revplay.util.LoggerUtil.logError("Error in addFavorite: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error adding favorite", e);
        }
    }

    @Override
    public boolean removeFavorite(int userId, int songId) {
        String sql = "DELETE FROM FAVORITE_SONG WHERE user_id = ? AND song_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, songId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in removeFavorite: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error removing favorite", e);
        }
    }

    @Override
    public List<Song> getFavoriteSongs(int userId) {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT s.*, g.genre_name FROM SONG s " +
                "JOIN FAVORITE_SONG f ON s.song_id = f.song_id " +
                "LEFT JOIN GENRE g ON s.genre_id = g.genre_id " +
                "WHERE f.user_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
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
                song.setFileUrl(rs.getString("file_url"));
                try {
                    song.setGenreName(rs.getString("genre_name"));
                } catch (Exception e) {
                }
                songs.add(song);
            }
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in getFavoriteSongs: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error fetching favorite songs", e);
        }
        return songs;
    }

    @Override
    public boolean isFavorite(int userId, int songId) {
        String sql = "SELECT * FROM FAVORITE_SONG WHERE user_id = ? AND song_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, songId);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in isFavorite: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error checking favorite status", e);
        }
        //return false;
    }
}
