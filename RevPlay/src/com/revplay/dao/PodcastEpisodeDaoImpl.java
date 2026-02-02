package com.revplay.dao;

import com.revplay.model.PodcastEpisode;
import com.revplay.util.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PodcastEpisodeDaoImpl implements IPodcastEpisodeDao {

    @Override
    public boolean addEpisode(PodcastEpisode episode) {
        String sql = "INSERT INTO PODCAST_EPISODE (podcast_id, title, duration_seconds, release_date, file_url, play_count, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, episode.getPodcastId());
            ps.setString(2, episode.getTitle());
            ps.setInt(3, episode.getDurationSeconds());
            ps.setDate(4, Date.valueOf(episode.getReleaseDate()));
            ps.setString(5, episode.getFileUrl());
            ps.setInt(6, episode.getPlayCount());
            ps.setTimestamp(7, Timestamp.valueOf(episode.getCreatedAt()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in addEpisode: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error adding episode", e);
        }
    }

    @Override
    public List<PodcastEpisode> getEpisodesByPodcast(int podcastId) {
        List<PodcastEpisode> list = new ArrayList<>();
        String sql = "SELECT * FROM PODCAST_EPISODE WHERE podcast_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, podcastId);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                list.add(mapEpisode(rs));
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in getEpisodesByPodcast: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error fetching episodes", e);
        }
        return list;
    }

    @Override
    public void playEpisode(int episodeId) {
        String sql = "UPDATE PODCAST_EPISODE SET play_count = play_count + 1 WHERE episode_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, episodeId);
            ps.executeUpdate();
            System.out.println("▶️ Listening to Episode ID: " + episodeId);
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in playEpisode: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error playing episode", e);
        }
    }

    @Override
    public List<PodcastEpisode> searchEpisodesByPodcastTitle(String podcastTitle) {
        List<PodcastEpisode> list = new ArrayList<>();
        String sql = "SELECT e.* FROM PODCAST_EPISODE e JOIN PODCAST p ON e.podcast_id = p.podcast_id WHERE p.title LIKE ?";
        try (Connection conn = JDBCUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + podcastTitle + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                list.add(mapEpisode(rs));
        } catch (SQLException e) {
            com.revplay.util.LoggerUtil.logError("Error in searchEpisodesByPodcastTitle: " + e.getMessage(), e);
            throw new com.revplay.exception.RevPlayException("Error searching episodes", e);
        }
        return list;
    }

    private PodcastEpisode mapEpisode(ResultSet rs) throws SQLException {
        return new PodcastEpisode(
                rs.getInt("episode_id"),
                rs.getInt("podcast_id"),
                rs.getString("title"),
                rs.getInt("duration_seconds"),
                rs.getDate("release_date").toLocalDate(),
                rs.getString("file_url"),
                rs.getInt("play_count"),
                rs.getTimestamp("created_at").toLocalDateTime());
    }
}
