package com.revplay.dao;

import com.revplay.model.ListeningHistory;
import com.revplay.model.Song;
import com.revplay.util.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ListeningHistoryDaoImpl implements IListeningHistoryDao {

    @Override
    public boolean addHistory(ListeningHistory history) {
        String sql = "INSERT INTO LISTENING_HISTORY (user_id, song_id, played_at, action_type) VALUES (?, ?, ?, ?)";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, history.getUserId());
            ps.setInt(2, history.getSongId());
            ps.setTimestamp(3, Timestamp.valueOf(history.getPlayedAt()));
            ps.setString(4, history.getActionType());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in addHistory: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error adding listening history", e);
        }
    }

    @Override
    public List<Song> getUserHistory(int userId) {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT s.*, h.played_at FROM SONG s " +
                "JOIN LISTENING_HISTORY h ON s.song_id = h.song_id " +
                "WHERE h.user_id = ? " +
                "ORDER BY h.played_at DESC";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Song song = new Song();
                song.setSongId(rs.getInt("song_id"));
                song.setTitle(rs.getString("title"));
                song.setArtistId(rs.getInt("artist_id"));
                song.setGenreId(rs.getInt("genre_id"));
                song.setFileUrl(rs.getString("file_url"));
                songs.add(song);
            }
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in getUserHistory: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error fetching listening history", e);
        }
        return songs;
    }
}
