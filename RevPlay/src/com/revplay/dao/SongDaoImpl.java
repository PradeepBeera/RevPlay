package com.revplay.dao;

import com.revplay.model.Song;
import com.revplay.util.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongDaoImpl implements ISongDao {

    @Override
    public boolean addSong(Song song) {
        String sql = "INSERT INTO SONG (title, album_id, artist_id, genre_id, duration_seconds, release_date, play_count, is_active, file_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, song.getTitle());
            if (song.getAlbumId() != null && song.getAlbumId() > 0)
                ps.setInt(2, song.getAlbumId());
            else
                ps.setNull(2, Types.INTEGER);
            ps.setInt(3, song.getArtistId());
            ps.setInt(4, song.getGenreId());
            ps.setInt(5, song.getDurationSeconds());
            ps.setDate(6, Date.valueOf(song.getReleaseDate()));
            ps.setInt(7, song.getPlayCount());
            ps.setString(8, "ACTIVE");
            ps.setString(9, song.getFileUrl()); // Added file_url

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in addSong: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error adding song", e);
        }
    }

    @Override
    public Song getSongById(int songId) {
        String sql = "SELECT s.*, g.genre_name FROM SONG s LEFT JOIN GENRE g ON s.genre_id = g.genre_id WHERE s.song_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, songId);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return mapResultSetToSong(rs);
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in getSongById: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error fetching song by ID", e);
        }
        return null;
    }

    // New method to get ALL songs for browse functionality
    public List<Song> getAllSongs() {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT s.*, g.genre_name FROM SONG s LEFT JOIN GENRE g ON s.genre_id = g.genre_id";
        try (Connection conn = JDBCUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next())
                songs.add(mapResultSetToSong(rs));
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in getAllSongs: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error fetching all songs", e);
        }
        return songs;
    }

    @Override
    public List<Song> getSongsByArtistId(int artistId) {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT s.*, g.genre_name FROM SONG s LEFT JOIN GENRE g ON s.genre_id = g.genre_id WHERE s.artist_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, artistId);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                songs.add(mapResultSetToSong(rs));
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in getSongsByArtistId: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error fetching songs by artist", e);
        }
        return songs;
    }

    @Override
    public List<Song> getSongsByAlbumId(int albumId) {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT s.*, g.genre_name FROM SONG s LEFT JOIN GENRE g ON s.genre_id = g.genre_id WHERE s.album_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, albumId);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                songs.add(mapResultSetToSong(rs));
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in getSongsByAlbumId: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error fetching songs by album", e);
        }
        return songs;
    }

    @Override
    public List<Song> searchSongs(String keyword) {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT s.*, g.genre_name FROM SONG s LEFT JOIN GENRE g ON s.genre_id = g.genre_id WHERE s.title LIKE ? OR g.genre_name LIKE ?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                songs.add(mapResultSetToSong(rs));
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in searchSongs: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error searching songs", e);
        }
        return songs;
    }

    @Override
    public boolean deleteSong(int songId) {
        String sql = "DELETE FROM SONG WHERE song_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, songId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in deleteSong: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error deleting song", e);
        }
    }

    @Override
    public boolean incrementPlayCount(int songId) {
        String sql = "UPDATE SONG SET play_count = play_count + 1 WHERE song_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, songId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in incrementPlayCount: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error incrementing play count", e);
        }
    }

    private Song mapResultSetToSong(ResultSet rs) throws SQLException {
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
        song.setFileUrl(rs.getString("file_url")); // Map file_url

        try {
            song.setGenreName(rs.getString("genre_name"));
        } catch (SQLException e) {
        }

        return song;
    }
}
